package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

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
        if (message instanceof SensorValue) {
            this.loggingAdapter.info(message.toString());
            SensorValue sensorValue = (SensorValue) message;

            String name = sensorValue.getName();
            String value = sensorValue.getValue();
            long timestamp = sensorValue.getTimestamp();

            Class<?> cepEventClass = Class.forName(name);

            CEPEvent event = (CEPEvent) cepEventClass.newInstance();

            event.setName(name);
            event.setValue(value);
            event.setTimeStamp(timestamp);

            cepRT.sendEvent(event);
        }
    }
}
