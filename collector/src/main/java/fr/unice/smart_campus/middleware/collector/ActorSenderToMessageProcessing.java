package fr.unice.smart_campus.middleware.collector;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

public class ActorSenderToMessageProcessing extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private ActorSelection messageProcessingReceiver;

    public ActorSenderToMessageProcessing() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        messageProcessingReceiver = this.getContext().actorSelection("akka.tcp://MessageProcessingActorSystem@52.16.33.142:2554/user/ActorReceiverFromCollector");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            this.loggingAdapter.info("Send message to Message Processing : " + message);
            messageProcessingReceiver.tell(message, this.sender());
        }
    }
}
