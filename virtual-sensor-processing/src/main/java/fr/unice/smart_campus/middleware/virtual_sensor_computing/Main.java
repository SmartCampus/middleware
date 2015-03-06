package fr.unice.smart_campus.middleware.virtual_sensor_computing;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import fr.unice.smart_campus.middleware.akka.actor.ScriptEvaluatorActor;

/**
 * Main class to launch the actor
 */
public class Main {
    public static void main(String[] args) {
        // creation of the actor system (Actor factory)
        ActorSystem actorSystem = ActorSystem.create("ActorSystemFactory", ConfigFactory.load());
        actorSystem.actorOf(Props.create(ScriptEvaluatorActor.class), "ScriptEvaluatorActor");
    }
}
