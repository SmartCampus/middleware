package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.json.JSONObject;
import sensor.SensorValue;

public class CEPInterfaceActor extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private CEPEngine cepRT;

    public CEPInterfaceActor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    public CEPInterfaceActor(CEPEngine cepRT) {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        this.cepRT = cepRT;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            this.loggingAdapter.info("___________  cep interface actor __________" + message.toString());
            // TODO change event
            JSONObject jsonObject = new JSONObject((String) message);

            RoomSensorEvent tick = new RoomSensorEvent(jsonObject.getString("v"), jsonObject.getString("n"), jsonObject.getString("t"));

            cepRT.sendEvent(tick);
        }
        if(message instanceof SensorValue){
            this.loggingAdapter.info("................."+message.toString());
        }
    }
}
