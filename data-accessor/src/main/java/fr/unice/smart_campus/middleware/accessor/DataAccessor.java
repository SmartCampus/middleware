package fr.unice.smart_campus.middleware.accessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


/**
 * DataAccessor
 */
public class DataAccessor {

	public Connection connection;

	public DataAccessor () {
		connection = null;
		try {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream("sensorsdata-database.properties"));

			String connectionStr = "jdbc:postgresql://" + properties.get("hostname") + ":" + properties.get("port")
					+ "/" + properties.get("dbname");

			connection = DriverManager.getConnection(connectionStr,
					(String) properties.get("username"), (String) properties.get("password"));

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
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


	public String getDataFromSensor (String idSensor, long beg, long end) {

		String selectSQL = "SELECT * FROM \"public\".\"SensorsData\" WHERE sensor_id = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();

		try {
			ps = connection.prepareStatement(selectSQL);
			ps.setString(1, idSensor);
			rs = ps.executeQuery();

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("date", rs.getString("sensor_date"));
				obj.put("value", rs.getString("sensor_value"));
				jsonArray.put(obj);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		jsonObject.put("id", idSensor);
		jsonObject.put("values", jsonArray);
		String data = jsonObject.toString();

		return data;
	}
}
