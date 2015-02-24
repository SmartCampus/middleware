package fr.unice.smart_campus.middleware.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import config.SensorParams;
import config.SensorType;


import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clement0210 on 15-02-18.
 */
public class SensorsConfigInputDataAccess {

    private MongoClient mongoClient;
    private DB db;
    private DBCollection coll;


    public SensorsConfigInputDataAccess(){
        try {
            mongoClient= new MongoClient();
            db = mongoClient.getDB( "ConfigDatabase" );
            coll = db.getCollection("sensors");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


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
        return new SensorParams(obj);

    }
}
