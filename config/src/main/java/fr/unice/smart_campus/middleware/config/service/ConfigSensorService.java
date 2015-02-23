package fr.unice.smart_campus.middleware.config.service;

import config.SensorParams;
import fr.unice.smart_campus.middleware.config.SensorsConfigInputDataAccess;
import fr.unice.smart_campus.middleware.config.SensorsConfigOutputDataAccess;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by clement0210 on 22/02/15.
 */
@Path("/")

public class ConfigSensorService {

    SensorsConfigInputDataAccess sensorsConfigInputDataAccess=new SensorsConfigInputDataAccess();
    SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess=new SensorsConfigOutputDataAccess();

    @PUT
    @Produces("text/plain")
    @Path("/sensors_params")
    public boolean addSensor(SensorParams sensorParams){
        return sensorsConfigOutputDataAccess.saveSensorParams(sensorParams);
    }

    @GET
    @Produces("application/json")
    @Path("/sensors_params/{sensor_id}")
    public SensorParams getSensor(@PathParam("sensor_id") String sensorID){
        return sensorsConfigInputDataAccess.getSensor(sensorID);
    }
    @GET
    @Produces("application/json")
    @Path("/sensors_params/physicals")
    public List<SensorParams> getPhysicalSensors(){
        return sensorsConfigInputDataAccess.getAllPhysicalSensors();
    }
    @GET
    @Produces("application/json")
    @Path("/sensors_params/virtuals")
    public List<SensorParams> getVirtualSensors(){
        return sensorsConfigInputDataAccess.getAllPhysicalSensors();
    }
}
