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
import fr.unice.smart_campus.middleware.model.config.SensorParams;
import fr.unice.smart_campus.middleware.model.config.SensorType;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clement0210 on 15-02-18.
 * This class is the input DAO for the sensors params database
 */
public class SensorsConfigInputDAO {

    private MongoClient mongoClient;
    private DB db;
    private DBCollection coll;

    /**
     * Default constructor
     */
    public SensorsConfigInputDAO(){
        try {
            mongoClient= new MongoClient();
            db = mongoClient.getDB( "ConfigDatabase" );
            coll = db.getCollection("sensors");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get virtual sensor from parents sensors
     * @param sensorParamsList
     * @return
     */
    public SensorParams getSensorFormParentsSensors(List<String> sensorParamsList){
        Cursor cursor = coll.find();
        try {
            while(cursor.hasNext()) {
                SensorParams sensorParams=new SensorParams(cursor.next());
                if(sensorParams.getParentSensors().containsAll(sensorParamsList) && sensorParamsList.containsAll(sensorParams.getParentSensors())){
                    return sensorParams;
                }

            }
        } finally {
            cursor.close();
        }
        return null;
    }

    /**
     * Method to get all physical sensors
     * @return
     */
    public List<SensorParams> getAllPhysicalSensors(){
        List<SensorParams> sensorParamsList=new ArrayList<SensorParams>();
        BasicDBObject query = new BasicDBObject(SensorParams.SENSORTYPE_COLUMN,SensorType.PHYSICAL.name().toLowerCase());
        Cursor cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                sensorParamsList.add(new SensorParams(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return sensorParamsList;
    }

    /**
     * Method to get all virtual sensors
     * @return
     */
    public List<SensorParams> getAllVirtualSensors(){
        List<SensorParams> sensorParamsList=new ArrayList<SensorParams>();
        BasicDBObject query = new BasicDBObject(SensorParams.SENSORTYPE_COLUMN,SensorType.VIRTUAL_COMPOSITE.name().toLowerCase());
        Cursor cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                sensorParamsList.add(new SensorParams(cursor.next()));
            }
        } finally {
            cursor.close();
        }

        query = new BasicDBObject(SensorParams.SENSORTYPE_COLUMN,SensorType.VIRTUAL_FILTER.name().toLowerCase());
        cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                sensorParamsList.add(new SensorParams(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return sensorParamsList;
    }
    /**
     * @return JSON String list of sensors
     */
    public SensorParams getSensor(String idSensor) {
        BasicDBObject query = new BasicDBObject(SensorParams.NAME_COLUMN,idSensor);

        DBObject obj = coll.findOne(query);
        if(obj==null){
            return null;
        }
        return new SensorParams(obj);

    }
}
