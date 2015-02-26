package fr.unice.smart_campus.middleware.akka.actor;

import fr.unice.smart_campus.middleware.model.sensor.SensorValue;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.database.SensorsDataOutputDataAccess;

/**
 * DatabaseAccessActor is an Akka Actor.
 * This class receive an event and process it.
 * The process consist of putting the data received (an SensorValue) into the database Sensor's data.
 */
public class DatabaseAccessActor extends UntypedActor {

    private SensorsDataOutputDataAccess sensorsDataOutputDataAccess;
    private LoggingAdapter loggingAdapter;

    public DatabaseAccessActor() throws Exception {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        sensorsDataOutputDataAccess = new SensorsDataOutputDataAccess();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SensorValue){
            this.loggingAdapter.info(message.toString());
            SensorValue sensorValueMessage = (SensorValue) message;
            sensorsDataOutputDataAccess.saveSensorData(sensorValueMessage.getName(), String.valueOf(sensorValueMessage.getTimestamp()), sensorValueMessage.getValue());
        }
    }
}
