package fr.unice.smart_campus.middleware.config.service;

/*
 * #%L
 * config
 * %%
 * Copyright (C) 2015 - 2016 Université de Nice Sophia-Antipolis (UNS) - Centre National de la Recherche Scientifique (CNRS)
 * %%
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Université de Nice Sophia-Antipolis (UNS) -
 * Centre National de la Recherche Scientifique (CNRS)
 * Copyright © 2015 UNS, CNRS
 * 
 * 
 *   Authors: SmartCampus Team - http://smartcampus.github.io/sc-contacts/
 * 
 *   Architects: Sébastien Mosser – Laboratoire I3S – mosser@i3s.unice.fr
 *               Michel Riveill - Laboratoire I3S - riveill@i3s.unice.fr
 * #L%
 */

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
