package fr.unice.smart_campus.middleware.processor;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

public class ActorReceiverFromCollector extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private ActorSelection cepSender;
    private ActorSelection databaseSender;

    public ActorReceiverFromCollector() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        cepSender = this.getContext().actorSelection("akka://MessageProcessingActorSystem/user/ActorSenderToCEP");
        databaseSender = this.getContext().actorSelection("akka://MessageProcessingActorSystem/user/ActorSenderToDatabase");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            databaseSender.tell(message, this.sender());
            cepSender.tell(message, this.sender());
        }
    }
}
