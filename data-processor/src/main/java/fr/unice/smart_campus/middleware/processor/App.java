package fr.unice.smart_campus.middleware.processor;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * Message processor application
 * Consumes messages from the message queue, process data and store raw sensor data into the database
 */
public class App extends UntypedActor implements MessageListener {

    private OutputDataAccess outputDataAccess;
    private ActorRef actorRef;

    public App() throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ActorSystemFactory", ConfigFactory.load());
        this.actorRef = actorSystem.actorOf(Props.create(ActorSenderToCEP.class));

        // Create output proxy
        outputDataAccess = new OutputDataAccess();

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

                // creation of the actor that has to send an event to the CEP Engine to allowed the process the data.
                this.actorRef.tell(jsonObject.toString(),ActorRef.noSender());

                // Save sensor data into the database
				outputDataAccess.saveSensorData(name, time, value);

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onReceive(Object message) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ActorSystemFactory", ConfigFactory.load());
        actorSystem.actorOf(Props.create(App.class));
    }
}
