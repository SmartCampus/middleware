package fr.unice.smart_campus.middleware.dataaccessor;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class DataAccessor
{
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
