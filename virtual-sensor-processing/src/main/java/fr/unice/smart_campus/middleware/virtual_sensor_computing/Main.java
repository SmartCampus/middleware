package fr.unice.smart_campus.middleware.virtual_sensor_computing;

import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;

public class Main {
    public static void main(String[] args) {
        // creation of the actor system (Actor factory)
        ActorSystem actorSystem = ActorSystem.create("ActorSystemFactory", ConfigFactory.load());
        //actorSystem.actorOf(Props.create(ScriptEvaluatorActor.class), "ScriptEvaluatorActor");

        //TODO virer nemo
//        ActorRef nemo = actorSystem.actorOf(Props.create(ScriptEvaluatorActor.class), "PouitPouitActor");
//        TypedSensorValueList sensors = new TypedSensorValueList();
//        sensors.getSensorValues().add(new TypedSensorValue("TEMP_443",123456789l,"12", SensorValueType.DOUBLE));
//        sensors.getSensorValues().add(new TypedSensorValue("TEMP_444",423456789l,"0.2", SensorValueType.DOUBLE));
//        nemo.tell(sensors,ActorRef.noSender());
    }
}
