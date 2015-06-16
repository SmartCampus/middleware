package fr.unice.smart_campus.middleware.collector;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

/**
 * Actor to send message to the Message Processing component
 */
public class ActorSenderToMessageProcessing extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private ActorSelection messageProcessingReceiver;

    /**
     * Default constructor
     */
    public ActorSenderToMessageProcessing() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        messageProcessingReceiver = this.getContext().actorSelection("akka.tcp://MessageProcessingActorSystem@172.31.39.206:2554/user/ActorReceiverFromCollector");
    }

    /**
     * Method to handle new message
     * @param message a message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            this.loggingAdapter.info("Send message to Message Processing : " + message);
            messageProcessingReceiver.tell(message, this.sender());
        }
    }
}
