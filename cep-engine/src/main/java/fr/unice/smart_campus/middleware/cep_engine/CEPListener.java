package fr.unice.smart_campus.middleware.cep_engine;

import sensor.SensorValue;
import akka.actor.ActorRef;
import com.espertech.esper.client.*;

/**
 * Listener of the event from the CEP engine.
 * When it receive a message from the CEP engine, it transfers the message to a remote Akka actor
 */
public class CEPListener implements UpdateListener{
    public ActorRef actorRef;
    public CEPListener(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public void update(final EventBean[] newData, EventBean[] oldData) {
        CEPEvent message = (CEPEvent)newData[0].getUnderlying();
        System.out.println("Send message to Akka actor : " + message);

        SensorValue sensorValueToSend = new SensorValue(message.getName(), Long.parseLong(message.getTimeStamp()),message.getValue());
        this.actorRef.tell(sensorValueToSend, ActorRef.noSender());
    }
}
