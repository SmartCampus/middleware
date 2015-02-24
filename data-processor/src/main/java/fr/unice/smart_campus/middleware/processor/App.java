package fr.unice.smart_campus.middleware.processor;

import akka.actor.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigFactory;

import fr.unice.smart_campus.middleware.database.SensorsDataOutputDataAccess;


import akka.actor.ActorRef;

import org.json.JSONObject;
import org.json.JSONTokener;
import sensor.SensorValue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;


/**
 * Message processor application
 * Consumes messages from the message queue, process data and store raw sensor data into the database
 */
public class App implements MessageListener {

    private SensorsDataOutputDataAccess sensorsDataOutputDataAccess;
    private ActorRef actorRef;

    public App() throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ActorSystemFactory", ConfigFactory.load());
        this.actorRef = actorSystem.actorOf(Props.create(ActorSenderToCEP.class));

        // Create output proxy
        sensorsDataOutputDataAccess = new SensorsDataOutputDataAccess();

        // Create input listener
        InputDataAccess.createMessageListener(this);
    }


    /**
     * Executed when a message is received from the message queue
     *
     * @param message The sensor data message
     */
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String jsonString = ((TextMessage) message).getText();

                // TODO: TEMP: Print received JSON message
                System.out.println(jsonString);

                // Extract sensor information
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
                String name = jsonObject.getString("n");
                String time = jsonObject.getString("t");
                String value = jsonObject.getString("v");

                ObjectMapper mapper = new ObjectMapper();

                SensorValue sensorValue = mapper.readValue(jsonString, SensorValue.class);

                // creation of the actor that has to send an event to the CEP Engine to allowed the process the data.
                this.actorRef.tell(sensorValue,ActorRef.noSender());

                // Save sensor data into the database
                sensorsDataOutputDataAccess.saveSensorData(name, time, value);

            } catch (JMSException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
}
