package fr.unice.smart_campus.middleware.config.service;

import fr.unice.smart_campus.middleware.model.config.SensorParams;
import fr.unice.smart_campus.middleware.config.SensorsConfigInputDAO;
import fr.unice.smart_campus.middleware.config.SensorsConfigOutputDAO;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clement0210 on 22/02/15.
 * Service that expose sensor parameters DAO
 */
@Path("/")

public class ConfigSensorService {

    SensorsConfigInputDAO sensorsConfigInputDAO =new SensorsConfigInputDAO();
    SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();

    @PUT
    @Produces("text/plain")
    @Consumes("application/json")
    @Path("/sensors_params")
    public boolean addSensor(SensorParams sensorParams){
        return sensorsConfigOutputDAO.saveSensorParams(sensorParams);
    }

    @GET
    @Produces("application/json")
    @Path("/sensors_params/{sensor_id}")
    public SensorParams getSensor(@PathParam("sensor_id") String sensorID){
        return sensorsConfigInputDAO.getSensor(sensorID);
    }

    @GET
    @Produces("application/json")
    @Path("/sensors_params/{sensor_id}/valuetype")
    public String getSensorValueType(@PathParam("sensor_id") String sensorID){

        SensorParams sensorParams= sensorsConfigInputDAO.getSensor(sensorID);
        return sensorParams.getValueType().name();
    }
    @GET
    @Produces("application/json")
    @Path("/sensors_params/physicals")
    public List<SensorParams> getPhysicalSensors(){
        return sensorsConfigInputDAO.getAllPhysicalSensors();
    }


    @GET
    @Produces("application/json")
    @Path("/sensors_params/physicals/names")
    public List<String> getPhysicalSensorsNames(){

        List<SensorParams> sensorParamses= sensorsConfigInputDAO.getAllPhysicalSensors();
        List<String> res=new ArrayList<String>();
        for(SensorParams sensorParams:sensorParamses){
            res.add(sensorParams.getName());
        }
        return res;
    }


    @GET
    @Produces("application/json")
    @Path("/sensors_params/virtuals")
    public List<SensorParams> getVirtualSensors(){
        return sensorsConfigInputDAO.getAllVirtualSensors();
    }

    @GET
    @Produces("application/json")
    @Path("/sensors_params/virtual")
    public SensorParams getSensorFormParentsSensors(@QueryParam("parents") final List<String> sensors){
        return sensorsConfigInputDAO.getSensorFormParentsSensors(sensors);
    }
}
