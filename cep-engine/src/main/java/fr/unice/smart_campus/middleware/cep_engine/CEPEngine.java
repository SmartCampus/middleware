package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.ActorRef;
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
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;

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
    private List<SensorParams> physicalSensors;
    private List<SensorParams> virtualSensors;

    private static String ALL_PHYSICAL = "http://52.16.33.142:8082/config/sensors_params/physicals/names";
    private static String ALL_VIRTUAL = "http://52.16.33.142:8082/config/sensors_params/virtuals";

    public CEPEngine() throws Exception {
        this.physicalSensors = new ArrayList<SensorParams>();
        this.virtualSensors = new ArrayList<SensorParams>();

        this.init();
    }

    private void getAllSensorsDefinition() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Get all physical
        String allPhysicalJson = this.getHttpResult(ALL_PHYSICAL);
        this.physicalSensors = mapper.readValue(allPhysicalJson, new TypeReference<List<SensorParams>>(){});

        // Get all virtual
        String allvirtualJson = this.getHttpResult(ALL_VIRTUAL);
        this.virtualSensors = mapper.readValue(allvirtualJson, new TypeReference<List<SensorParams>>(){});
    }


    private void init() throws Exception {
        // Retrieve sensors Param from ConfigDatabase
        this.getAllSensorsDefinition();

        // TODO To delete
//        List<SensorParams> physicalSensors = new ArrayList<SensorParams>();
//        physicalSensors.add(new SensorParams("TMP_333", "", "", SensorType.PHYSICAL, SensorValueType.DOUBLE, 2, null));
//        physicalSensors.add(new SensorParams("TMP_334", "", "", SensorType.PHYSICAL, SensorValueType.DOUBLE, 2, null));
//
//        List<String> parents = new ArrayList<String>();
//        parents.add("TMP_333");
//        parents.add("TMP_334");
//
//        List<SensorParams> virtualSensors = new ArrayList<SensorParams>();
//        virtualSensors.add(new SensorParams("CV1", "", "", SensorType.VIRTUAL_FILTER, SensorValueType.DOUBLE, 2, parents));


        // create Akka ActorSystem and actors
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        ActorRef actorRef = system.actorOf(FromConfig.getInstance().props(Props.create(ScriptEvaluatorActor.class)), "remotePool");

        // Create the configuration of our CEPEngine
        Configuration cepConfig = new Configuration();
        // Create events class and register them in CEPEngine
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(CEPEvent.class));

        for (SensorParams sensorParams : physicalSensors) {
            String name = sensorParams.getName();
            generateCepEvent(pool, name);
            cepConfig.addEventType(name, name);
        }

        for (SensorParams sensorParams : virtualSensors) {
            String name = sensorParams.getName();
            generateCepEvent(pool, name);
            cepConfig.addEventType(name, name);
        }

        // Create and store CEPListener. One CEPListener is created for every SensorValueType
        Map<SensorValueType, CEPListener> cepListenerMap = new HashMap<SensorValueType, CEPListener>();
        for (SensorValueType type : SensorValueType.values()) {
            Class<? extends CEPListener> listenerClass = this.generateCepListener(pool, type.name() + "Listener");

            CEPListener listener = listenerClass.getConstructor(ActorRef.class).newInstance(actorRef);
            listener.setType(type);

            cepListenerMap.put(type, listener);
        }

        // Create our CEP Provider and get the runtime CEP
        EPServiceProvider cep = EPServiceProviderManager.getProvider("SmartCampusCepEngine", cepConfig);
        cepRT = cep.getEPRuntime();


        // Get the EPAdimistrator. We use it to declare our rules
        EPAdministrator cepAdm = cep.getEPAdministrator();

        for (SensorParams virtualSensorParam : virtualSensors) {
            StringBuilder statement = new StringBuilder();

            List<ParentSensor> parentSensors = virtualSensorParam.getParentSensors();

            String timeParams = ".win:time(30 minutes)";

            StringBuilder selectString = new StringBuilder();
            selectString.append(" select ");
            selectString.append(" a0 ");

            StringBuilder fromString = new StringBuilder();
            fromString.append(" from ");
            fromString.append(parentSensors.get(0).getName());
            fromString.append(timeParams);
            fromString.append(" a0 ");

            for (int i = 1; i < parentSensors.size(); i++) {
                selectString.append(" , a" + i + " ");

                fromString.append(" , ");
                fromString.append(parentSensors.get(i).getName());
                fromString.append(timeParams);
                fromString.append(" a" + i + " ");
            }

            statement.append(selectString.toString());
            statement.append(fromString.toString());

            // TODO A rendre super super beau !
            SensorParams s = new SensorParams();
            s.setName(parentSensors.get(0).getName());

            int indexPhysical = physicalSensors.indexOf(s);
            int indexVirtual = virtualSensors.indexOf(s);
            SensorValueType type;
            if(indexPhysical!=-1){
                type = physicalSensors.get(indexPhysical).getValueType();
            }else if(indexVirtual !=-1) {
                type = virtualSensors.get(indexVirtual).getValueType();
            }else{
                System.out.println("Error!!! Parent Sensor("+ parentSensors.get(0) +") not found for vitualSensor : " + virtualSensorParam.getName());
                continue;
            }

            CEPListener listener = cepListenerMap.get(type);

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
        CtClass cepµListenerSuperClass = pool.get(CEPListener.class.getName());
        CtClass cc = pool.makeClass(className, cepµListenerSuperClass);

        // Generate class
        return cc.toClass();
    }

    public void sendEvent(CEPEvent event) {
        System.out.println("Sending event to CEP : " + event.toString());
        cepRT.sendEvent(event);
    }

    private String getHttpResult(String urlToRead){
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
