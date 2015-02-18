package fr.unice.smart_campus.middleware.processor;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

import javax.jms.TextMessage;

public class ActorSenderToCEP extends UntypedActor{

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            this.sendEventToCEPEngine((String)message);
        }
    }

    /**
     * Creation of the actor that has to send an event
     * to the CEP Engine to allowed the process the data.
     * @param messageToSend
     */
    private void sendEventToCEPEngine(String messageToSend){
        ActorSelection actorSelection = this.getContext().actorSelection("akka.tcp://Simulation@localhost:2553/user/CEPInterfaceActor");
        actorSelection.tell(messageToSend, this.sender());
    }
}
