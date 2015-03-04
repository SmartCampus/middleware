package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.espertech.esper.client.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigFactory;
import fr.unice.smart_campus.middleware.model.config.ParentSensor;
import fr.unice.smart_campus.middleware.model.config.SensorParams;
import fr.unice.smart_campus.middleware.akka.actor.ScriptEvaluatorActor;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CEPEngine {

    private EPRuntime cepRT;
    private List<String> physicalSensors;
    private List<SensorParams> virtualSensors;

    private static String ALL_PHYSICAL = "http://52.16.33.142:8082/config/sensors_params/physicals/names";
    private static String ALL_VIRTUAL = "http://52.16.33.142:8082/config/sensors_params/virtuals";

    public CEPEngine() throws Exception {
        this.physicalSensors = new ArrayList<String>();
        this.virtualSensors = new ArrayList<SensorParams>();

        this.init();
    }

    private void getAllSensorsDefinition() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Get all physical
        String allPhysicalJson = this.getHttpResult(ALL_PHYSICAL);
        this.physicalSensors = mapper.readValue(allPhysicalJson, new TypeReference<List<String>>() {
        });

        // Get all virtual
        String allvirtualJson = this.getHttpResult(ALL_VIRTUAL);
        this.virtualSensors = mapper.readValue(allvirtualJson, new TypeReference<List<SensorParams>>() {
        });
    }

    private void init() throws Exception {
        // Retrieve sensors Param from ConfigDatabase
        this.getAllSensorsDefinition();

        // create Akka ActorSystem and actors
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        ActorSelection actorSelection = system.actorSelection("akka.tcp://ActorSystemFactory@localhost:2552/user/ScriptEvaluatorActor");
//        ActorRef actorRef = system.actorOf(FromConfig.getInstance().props(Props.create(ScriptEvaluatorActor.class)), "remotePool");

        // Create the configuration of our CEPEngine
        Configuration cepConfig = new Configuration();
        // Create events class and register them in CEPEngine
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(CEPEvent.class));

        for (String sensorParamsName : physicalSensors) {
            generateCepEvent(pool, sensorParamsName);
            cepConfig.addEventType(sensorParamsName, sensorParamsName);
        }

        for (SensorParams sensorParams : virtualSensors) {
            String name = sensorParams.getName();
            generateCepEvent(pool, name);
            cepConfig.addEventType(name, name);
        }

        // Create our CEP Provider and get the runtime CEP
        EPServiceProvider cep = EPServiceProviderManager.getProvider("SmartCampusCepEngine", cepConfig);
        cepRT = cep.getEPRuntime();


        // Get the EPAdimistrator. We use it to declare our rules
        EPAdministrator cepAdm = cep.getEPAdministrator();

        for (SensorParams virtualSensorParam : virtualSensors) {
            StringBuilder statement = new StringBuilder();
            Map<String, SensorValueType> parentValueTypeMap = new HashMap<String, SensorValueType>();

            String virtualSensorName = virtualSensorParam.getName();
            String virtualSensorScript = virtualSensorParam.getScript();
            List<ParentSensor> parentSensorsList = virtualSensorParam.getParentSensors();
            ParentSensor parentSensor;
            String parentSensorName;
            SensorValueType parentSensorValueType;

            String timeParams = ".win:time(30 minutes)";

            // First index of parentSensors. there is at least one parent !
            StringBuilder selectString = new StringBuilder();
            selectString.append(" select ");
            selectString.append(" a0 ");

            parentSensor = parentSensorsList.get(0);
            parentSensorName = parentSensor.getName();
            parentSensorValueType = parentSensor.getValueType();

            StringBuilder fromString = new StringBuilder();
            fromString.append(" from ");
            fromString.append(parentSensorName);
            fromString.append(timeParams);
            fromString.append(" a0 ");

            parentValueTypeMap.put(parentSensorName, parentSensorValueType);

            for (int i = 1; i < parentSensorsList.size(); i++) {
                selectString.append(" , a" + i + " ");

                parentSensor = parentSensorsList.get(i);
                parentSensorName = parentSensor.getName();
                parentSensorValueType = parentSensor.getValueType();

                fromString.append(" , ");
                fromString.append(parentSensorName);
                fromString.append(timeParams);
                fromString.append(" a" + i + " ");

                parentValueTypeMap.put(parentSensorName, parentSensorValueType);
            }

            statement.append(selectString.toString());
            statement.append(fromString.toString());

            // Create CEPListener. One CEPListener is created for every Virtual Sensor
            Class<? extends CEPListener> listenerClass = this.generateCepListener(pool, virtualSensorName + "Listener");
            CEPListener listener = listenerClass.getConstructor(ActorSelection.class).newInstance(actorSelection);
            listener.setVirtualSensorName(virtualSensorName);
            listener.setParentValueTypeMap(parentValueTypeMap);
            listener.setScript(virtualSensorScript);

            System.out.println(statement.toString());

            EPStatement cepStatement = cepAdm.createEPL(statement.toString());
            cepStatement.addListener(listener);
        }

        system.actorOf(Props.create(CEPInterfaceActor.class, this), "CEPInterfaceActor");
    }

    private Class<? extends CEPEvent> generateCepEvent(ClassPool pool, String className) throws Exception {
        CtClass cepEventSuperClass = pool.get(CEPEvent.class.getName());
        CtClass cc = pool.makeClass(className, cepEventSuperClass);

        // Generate class
        return cc.toClass();
    }

    private Class<? extends CEPListener> generateCepListener(ClassPool pool, String className) throws Exception {
        CtClass cepListenerSuperClass = pool.get(CEPListener.class.getName());
        CtClass cc = pool.makeClass(className, cepListenerSuperClass);

        // Generate class
        return cc.toClass();
    }

    public void sendEvent(CEPEvent event) {
        //System.out.println("Sending event to CEP : " + event.toString());
        cepRT.sendEvent(event);
    }

    private String getHttpResult(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseMessage();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
