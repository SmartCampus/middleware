package fr.unice.smart_campus.middleware.virtual_sensor_processing;

import akka.actor.*;
import akka.dispatch.Dispatchers;
import akka.dispatch.Mailboxes;
import akka.event.EventStream;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import fr.unice.smart_campus.middleware.akka.actor.DatabaseAccessActor;
import fr.unice.smart_campus.middleware.akka.actor.ScriptEvaluatorActor;
import groovy.lang.GroovyShell;
import scala.Function0;
import scala.collection.*;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import sensor.SensorValueType;
import sensor.TypedSensorValue;
import sensor.TypedSensorValueList;

public class Main {
    public static void main(String[] args) {
        // creation of the actor system (Actor factory)
        ActorSystem actorSystem = ActorSystem.create("ActorSystemFactory", ConfigFactory.load());
        actorSystem.actorOf(Props.create(DatabaseAccessActor.class), "DatabaseAccessActor");
        //TODO virer nemo
        ActorRef nemo = actorSystem.actorOf(Props.create(ScriptEvaluatorActor.class), "PouitPouitActor");
        TypedSensorValueList sensors = new TypedSensorValueList();
        sensors.getSensorValues().add(new TypedSensorValue("TEMP_443",123456789l,"12", SensorValueType.DOUBLE));
        sensors.getSensorValues().add(new TypedSensorValue("TEMP_444",423456789l,"0.2", SensorValueType.DOUBLE));
        nemo.tell(sensors,ActorRef.noSender());
    }
}
