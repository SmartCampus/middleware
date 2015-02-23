package fr.unice.smart_campus.middleware.config.service;

import fr.unice.smart_campus.middleware.config.SensorsConfigInputDataAccess;
import fr.unice.smart_campus.middleware.config.SensorsConfigOutputDataAccess;
import fr.unice.smart_campus.middleware.config.model.SensorParams;
import javax.ws.rs.*;

/**
 * Created by clement0210 on 22/02/15.
 */
@Path("/sensors_params")

public class ConfigSensorService {

    SensorsConfigInputDataAccess sensorsConfigInputDataAccess=new SensorsConfigInputDataAccess();
    SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess=new SensorsConfigOutputDataAccess();

    @PUT
    @Produces("text/plain")
    @Path("/")
    public boolean addSensor(SensorParams sensorParams){
        return sensorsConfigOutputDataAccess.saveSensorParams(sensorParams);
    }

    @GET
    @Produces("application/json")
    @Path("/{sensor_id}")
    public SensorParams getSensor(@PathParam("sensor_id") String sensorID){
        return sensorsConfigInputDataAccess.getSensors(sensorID);
    }
}
