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

    private String configDatabaseURL="http://" + System.getProperty("middleware.ip") + ":5000/sensors";
    private MongoClient mongoClient;
    private DB db;
    private DBCollection coll;


    public SensorsConfigInputDataAccess(){
        try {
            mongoClient= new MongoClient( System.getProperty("middleware.ip"), 5000 );
            db = mongoClient.getDB( "ConfigDatabase" );
            coll = db.getCollection("sensors");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public SensorParams getSensorFormParentsSensors(List<SensorParams> sensorParamsList){
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        parentsSensors.add("TEMP_444");
        SensorParams sensorParams1=new SensorParams("CV1","temp","int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;", SensorType.VIRTUAL_COMPOSITE,20,parentsSensors);
        SensorParams sensorParams2=new SensorParams("CV2","temp","int B=124; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;", SensorType.VIRTUAL_COMPOSITE,20,parentsSensors);
        List<SensorParams> sensorParamses=new ArrayList<SensorParams>();
        sensorParamses.add(sensorParams1);
        //sensorParamses.add(sensorParams2);
        return sensorParams1;
    }

    public List<SensorParams> getAllPhysicalSensors(){
        List<String> parentsSensors=new ArrayList<String>();
        SensorParams sensorParams1=new SensorParams("TEMP_443","temp","", SensorType.PHYSICAL,20,parentsSensors);
        SensorParams sensorParams2=new SensorParams("TEMP_444","temp","", SensorType.PHYSICAL,20,parentsSensors);
        List<SensorParams> sensorParamses=new ArrayList<SensorParams>();
        sensorParamses.add(sensorParams1);
        sensorParamses.add(sensorParams2);
        return sensorParamses;
        /**List<SensorParams> sensorParamsList=new ArrayList<SensorParams>();
        BasicDBObject query = new BasicDBObject(SensorParams.SENSORTYPE_COLUMN,SensorType.PHYSICAL);
        Cursor cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                sensorParamsList.add(new SensorParams(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return sensorParamsList;*/
    }
    public List<SensorParams> getAllVirtualSensors(){
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        parentsSensors.add("TEMP_444");
        SensorParams sensorParams1=new SensorParams("CV1","temp","int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;", SensorType.VIRTUAL_COMPOSITE,20,parentsSensors);
        SensorParams sensorParams2=new SensorParams("CV2","temp","int B=124; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;", SensorType.VIRTUAL_COMPOSITE,20,parentsSensors);
        List<SensorParams> sensorParamses=new ArrayList<SensorParams>();
        sensorParamses.add(sensorParams1);
        sensorParamses.add(sensorParams2);
        return sensorParamses;

        /**List<SensorParams> sensorParamsList=new ArrayList<SensorParams>();
        BasicDBObject query = new BasicDBObject(SensorParams.SENSORTYPE_COLUMN,SensorType.VIRTUAL_COMPOSITE);
        Cursor cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                sensorParamsList.add(new SensorParams(cursor.next()));
            }
        } finally {
            cursor.close();
        }

        query = new BasicDBObject(SensorParams.SENSORTYPE_COLUMN,SensorType.VIRTUAL_FILTER);
        cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                sensorParamsList.add(new SensorParams(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return sensorParamsList;*/
    }
    /**
     * @return JSON String list of sensors
     */
    public SensorParams getSensor(String idSensor) {
        BasicDBObject query = new BasicDBObject(SensorParams.NAME_COLUMN,idSensor);

        Cursor cursor = coll.find(query);

        try {
            while(cursor.hasNext()) {
                return new SensorParams(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return null;
    }
}
