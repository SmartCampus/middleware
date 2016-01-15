package fr.unice.smart_campus.middleware.config;

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

import com.mongodb.*;
import fr.unice.smart_campus.middleware.model.config.ParentSensor;
import fr.unice.smart_campus.middleware.model.config.SensorParams;
import fr.unice.smart_campus.middleware.model.config.SensorType;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is the output DAO for the sensors params database
 */
public class SensorsConfigOutputDAO {

    private MongoClient mongoClient;
    private DB db;
    private DBCollection coll;

    /**
     * Default constructor
     */
    public SensorsConfigOutputDAO(){
        try {
            mongoClient= new MongoClient();
            db = mongoClient.getDB( "ConfigDatabase" );
            coll = db.getCollection("sensors");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    /**
     * Save a sensor param data into the database
     *
     * @param sensorParams a sensor param to store
     */
    public boolean saveSensorParams (SensorParams sensorParams) {
        if(!check(sensorParams)){
            return false;
        }
        coll.insert(sensorParams.toDoc());
        return true;
    }


    /**
     * Check if all sensor params are ok
     * @param sensorParams a sensor param
     * @return true if it's ok
     */
    public boolean check(SensorParams sensorParams){
        boolean isPhysical= (SensorType.PHYSICAL.equals(sensorParams.getSensorType()) && (sensorParams.getParentSensors()==null || sensorParams.getParentSensors().size()==0));
        boolean isFilter=(SensorType.VIRTUAL_FILTER.equals(sensorParams.getSensorType()) && sensorParams.getParentSensors().size()==1);
        boolean isComposite = (SensorType.VIRTUAL_COMPOSITE.equals(sensorParams.getSensorType()) && sensorParams.getParentSensors().size()>=2);
        if(!isComposite && !isFilter && !isPhysical){
            return false;
        }
        if(isPhysical){
            if((sensorParams.getScript()!=null && !"".equals(sensorParams.getScript()))) {
                return false;
            }
            return true;
        }


        Pattern pattern = Pattern.compile("\\$\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(sensorParams.getScript());
        List<String> sensorsFromScript=new ArrayList<String>();
        while (matcher.find()) {

                try{
                    sensorsFromScript.add(matcher.group(1));
                    // We adapt the script to be compatible with the shell
                    sensorParams.setScript(sensorParams.getScript().replaceAll("\\$\\(" + matcher.group(1) + "\\)", matcher.group(1)));
                }
                catch (IndexOutOfBoundsException e){
                    return false;
                }

            }

        if(sensorsFromScript.size() < sensorParams.getParentSensors().size()){
            return false;
        }
        for(String sensor : sensorsFromScript){
            ParentSensor parentSensor=new ParentSensor();
            parentSensor.setName(sensor);
            if(!sensorParams.getParentSensors().contains(parentSensor)){
                return false;
            }
        }

        return true;
    }
}


