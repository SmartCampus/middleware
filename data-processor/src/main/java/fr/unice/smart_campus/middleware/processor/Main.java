package fr.unice.smart_campus.middleware.processor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;


/**
 * Message processor application
 * Consumes messages from the message queue, process data and store raw sensor data into the database
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("MessageProcessingActorSystem", ConfigFactory.load());
        actorSystem.actorOf(Props.create(ActorSenderToCEP.class), "ActorSenderToCEP");
        actorSystem.actorOf(Props.create(ActorSenderToDatabase.class), "ActorSenderToDatabase");
        actorSystem.actorOf(Props.create(ActorReceiverFromCollector.class), "ActorReceiverFromCollector");
    }
}
