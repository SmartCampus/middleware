package fr.unice.smart_campus.middleware.collector;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigFactory;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

import java.io.IOException;


/**
 * DataAccess class
 * Interface to post sensor values into the message queue
 */
public class DataAccess {
    private static DataAccess instance;
    private ActorRef senderToMessageProcessing;

    /**
     * Get the class instance
     */
    public static synchronized DataAccess getInstance() {
        if (instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }

    /**
     * Private constructor
     */
    private DataAccess() {
        ActorSystem actorSystem = ActorSystem.create("CollectorActorSystem", ConfigFactory.load());
        this.senderToMessageProcessing = actorSystem.actorOf(Props.create(ActorSenderToMessageProcessing.class), "ActorSenderToMessageProcessing");
    }


    /**
     * Post a sensor message to MessageProcessing
     *
     * @param jsonString The JSON string that contains sensor data
     */
    public void postMessage(String jsonString) throws IOException {
        // Extract sensor information
        ObjectMapper mapper = new ObjectMapper();

        SensorValue sensorValue = mapper.readValue(jsonString, SensorValue.class);

        this.senderToMessageProcessing.tell(sensorValue, ActorRef.noSender());
    }
}
