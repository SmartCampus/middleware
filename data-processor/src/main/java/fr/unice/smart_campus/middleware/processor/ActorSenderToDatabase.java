package fr.unice.smart_campus.middleware.processor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.database.SensorsDataOutputDataAccess;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

public class ActorSenderToDatabase extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private SensorsDataOutputDataAccess sensorsDataOutputDataAccess;

    public ActorSenderToDatabase() throws Exception {
        this.sensorsDataOutputDataAccess = new SensorsDataOutputDataAccess();
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            this.sendEventToDatabase((SensorValue) message);
        }
    }

    /**
     * Creation of the actor that has to send an event
     * to the CEP Engine to allowed the process the data.
     *
     * @param messageToSend
     */
    private void sendEventToDatabase(SensorValue messageToSend) {
        this.loggingAdapter.info("Save message in database : " + messageToSend);
        // Save sensor data into the database
        sensorsDataOutputDataAccess.saveSensorData(messageToSend.getName(), String.valueOf(messageToSend.getTimestamp()), messageToSend.getValue());
    }
}
