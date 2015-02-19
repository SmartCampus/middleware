package fr.unice.smart_campus.middleware.akka.actor;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actor extends UntypedActor {
    private LoggingAdapter loggingAdapter;

    public Actor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            this.loggingAdapter.info(message.toString());

            ActorSelection actorSelection = this.getContext().actorSelection("akka.tcp://Simulation@localhost:2553/user/CEPInterfaceActor");
            actorSelection.tell("Romain et Jerome chez Twitter", this.sender());
        }
    }
}
