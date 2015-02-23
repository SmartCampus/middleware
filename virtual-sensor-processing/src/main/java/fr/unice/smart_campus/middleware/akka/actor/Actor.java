package fr.unice.smart_campus.middleware.akka.actor;

import sensor.SensorValue;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.json.JSONObject;

import java.util.Random;

public class Actor extends UntypedActor {
    private LoggingAdapter loggingAdapter;

    public Actor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SensorValue){
            this.loggingAdapter.info("_-_-_"+message.toString());

            ActorSelection databaseAccess = this.getContext().actorSelection("akka://ActorSystemFactory/user/DatabaseAccessActor");
            databaseAccess.tell(message, this.self());

            if ((new Random().nextInt(4)) == 0) {
                ActorSelection actorSelection = this.getContext().actorSelection("akka.tcp://Simulation@localhost:2553/user/CEPInterfaceActor");
                SensorValue sensorValue = new SensorValue("testAkkaActor",12,"12343");
                actorSelection.tell(sensorValue, this.sender());
            }
        }
    }
}
