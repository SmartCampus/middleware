package fr.unice.smart_campus.middleware.cep_engine;

import SensorModel.SensorValue;
import akka.actor.ActorRef;
import com.espertech.esper.client.*;

public class CEPListener implements UpdateListener{
    public ActorRef actorRef;
    public CEPListener(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public void update(final EventBean[] newData, EventBean[] oldData) {
        String message = ((CEPEvent)newData[0].getUnderlying()).toString();
        System.out.println("Send message to Akka actor : " + message);

        // TODO : envoyer un message avec un vrai contenu
        SensorValue sensorValueToSend = new SensorValue("12",123645 ,"13");
        this.actorRef.tell(sensorValueToSend, ActorRef.noSender());
    }
}
