package fr.unice.smart_campus.middleware.accessor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.ds.PGPoolingDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DataAccessor
 */
public class DataAccessor {

	public Connection connection;

	public DataAccessor () {
		connection = null;
		try {

			Context initialContext = new InitialContext();
			if ( initialContext == null){
				System.err.println("Cannot get InitalContext");
			}
			else {
				// Get the data source from the context
				PGPoolingDataSource datasource = (PGPoolingDataSource)initialContext.lookup("java:/comp/env/jdbc/SensorsData");
				if (datasource != null){
					// Get the connection from the datasource
					connection = datasource.getConnection();
				}
				else {
					System.err.println("Failed to get datasource");
				}
			}

		} catch (SQLException ex){
			System.err.println("Cannot get connection: " + ex);
		} catch (Exception ex){
			System.err.println("General error on attempting connection: " + ex);
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
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		jsonObject.put("id", idSensor);
		jsonObject.put("values", jsonArray);
		String data = jsonObject.toString();


		return data;
	}

	/**
	 * To close and release connection
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
