package fr.unice.smart_campus.middleware.dataaccessor;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DataAccessor
 *
 */
public class DataAccessor
{
    public static Connection connection;

    public DataAccessor () {
        connection = null;
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("sensorsdata-database.properties"));

            String connectionStr = "jdbc:postgresql://" + properties.get("hostname") + ":" + properties.get("port")
                    + "/" + properties.get("dbname");

            connection = DriverManager.getConnection(connectionStr,
                    (String)properties.get("username"), (String)properties.get("password"));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return JSON String list of sensors
     */
    public String getSensors () {
        String sensors = "";

        //TODO Retrieve sensors list from config database

        /* TEST */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonArray.put("ARD-1");
        jsonArray.put("ARD-2");

        jsonObject.put("sensors", jsonArray);
        sensors = jsonArray.toString();
        /* */

        return sensors;
    }


    public String getDataFromSensor(String idSensor, long beg, long end) {
        String data = "";

        //TODO Retrieve data of sensor from database

        /* TEST */
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonArray.put("ARD-1");
        jsonArray.put("ARD-2");

        jsonObject.put("sensors", jsonArray);
        data = jsonArray.toString();
        /* */

        return data;
    }
}
