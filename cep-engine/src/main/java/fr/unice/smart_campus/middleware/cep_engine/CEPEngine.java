package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.espertech.esper.client.*;
import com.typesafe.config.ConfigFactory;
import config.SensorParams;
import config.SensorType;
import fr.unice.smart_campus.middleware.akka.actor.ScriptEvaluatorActor;
import fr.unice.smart_campus.middleware.config.SensorsConfigInputDataAccess;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import sensor.SensorValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CEPEngine {

    private EPRuntime cepRT;

    public CEPEngine() throws Exception {
        this.init();
    }

    private void init() throws Exception {
        // Retrieve sensors Param from ConfigDatabase
        SensorsConfigInputDataAccess sensorsConfigInputDataAccess = new SensorsConfigInputDataAccess();
        List<SensorParams> physicalSensors = sensorsConfigInputDataAccess.getAllPhysicalSensors();
        List<SensorParams> virtualSensors = sensorsConfigInputDataAccess.getAllVirtualSensors();

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
        system.actorOf(Props.create(CEPInterfaceActor.class, this), "CEPInterfaceActor");

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

            List<String> parentSensors = virtualSensorParam.getParentSensors();

            String timeParams = ".win:time(30 minutes)";

            StringBuilder selectString = new StringBuilder();
            selectString.append(" select ");
            selectString.append(" a0 ");

            StringBuilder fromString = new StringBuilder();
            fromString.append(" from ");
            fromString.append(parentSensors.get(0));
            fromString.append(timeParams);
            fromString.append(" a0 ");

            for (int i = 1; i < parentSensors.size(); i++) {
                selectString.append(" , a" + i + " ");

                fromString.append(" , ");
                fromString.append(parentSensors.get(i));
                fromString.append(timeParams);
                fromString.append(" a" + i + " ");
            }

            statement.append(selectString.toString());
            statement.append(fromString.toString());

            CEPListener listener = cepListenerMap.get(virtualSensorParam.getValueType());

            System.out.println(statement.toString());

            EPStatement cepStatement = cepAdm.createEPL(statement.toString());
            cepStatement.addListener(listener);
        }
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
}
