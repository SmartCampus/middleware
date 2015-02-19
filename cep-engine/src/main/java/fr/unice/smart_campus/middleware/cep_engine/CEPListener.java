package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.ActorRef;
import com.espertech.esper.client.*;

public class CEPListener implements UpdateListener{
    public ActorRef actorRef;
    public CEPListener(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public void update(final EventBean[] newData, EventBean[] oldData) {
        // TODO : ne plus mettre de string mais balader des Objects (mettre un jar ou ...)
        String message = ((CEPEvent)newData[0].getUnderlying()).toString();
        System.out.println("Send message to Akka actor : " + message);
        this.actorRef.tell(message, ActorRef.noSender());
    }
}
