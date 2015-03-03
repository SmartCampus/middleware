package fr.unice.smart_campus.middleware.processor;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

public class ActorSenderToCEP extends UntypedActor{
    private LoggingAdapter loggingAdapter;

    public ActorSenderToCEP() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            this.sendEventToCEPEngine((SensorValue)message);
        }
    }

    /**
     * Creation of the actor that has to send an event
     * to the CEP Engine to allowed the process the data.
     * @param messageToSend
     */
    private void sendEventToCEPEngine(SensorValue messageToSend){
        this.loggingAdapter.info("Send event to CEP Interface Actor : " + messageToSend);
        ActorSelection actorSelection = this.getContext().actorSelection("akka.tcp://Simulation@52.16.120.70:2553/user/CEPInterfaceActor");
        actorSelection.tell(messageToSend, this.sender());
    }
}
