package fr.unice.smart_campus.middleware.config;

import fr.unice.smart_campus.middleware.config.model.SensorParams;
import fr.unice.smart_campus.middleware.config.model.SensorType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OutputDataAccess {

    private Connection connection;


    /**
     * Create a new connection to the database
     */
    public OutputDataAccess () throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));

        String connectionStr = "jdbc:postgresql://" + properties.get("hostname") + ":" + properties.get("port")
                + "/" + properties.get("dbname");

        /**connection = DriverManager.getConnection(
                connectionStr,
                (String) properties.get("username"),
                (String) properties.get("password"));*/
    }


    /**
     * Save a sensor param data into the database
     *
     * @param sensorParams a sensor param to store
     */
    //TODO: Push in the database
    public boolean saveSensorParams (SensorParams sensorParams) {


        return check(sensorParams);
    }

    /**
     * Check if all sensor params are ok
     * @param sensorParams a sensor param
     * @return true if it's ok
     */
    private boolean check(SensorParams sensorParams){

        boolean isPhysical= (SensorType.PHYSICAL.equals(sensorParams.getSensorType()) && (sensorParams.getParentSensors()==null || sensorParams.getParentSensors().size()==0));
        boolean isFilter=(SensorType.VIRTUAL_FILTER.equals(sensorParams.getSensorType()) && sensorParams.getParentSensors().size()==1);
        boolean isComposite = (SensorType.VIRUTAL_COMPOSITE.equals(sensorParams.getSensorType()) && sensorParams.getParentSensors().size()>=2);
        if(!isComposite && !isFilter && !isPhysical){
            System.out.println("LAL");
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
                }
                catch (IndexOutOfBoundsException e){
                    return false;
                }

            }

        if(sensorsFromScript.size() < sensorParams.getParentSensors().size()){
            return false;
        }
        for(String sensor : sensorsFromScript){
            if(!sensorParams.getParentSensors().contains(sensor)){
                return false;
            }
        }

        return true;
    }
}


