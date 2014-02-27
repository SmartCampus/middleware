package fr.unice.smart_campus.middleware.accessor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.ds.PGPoolingDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DataAccessor class
 * To get access to data in SensorsData database.
 */
public class DataAccessor {

	public Connection connection;

	/**
	 * DataAccessor Constructor to access SensorsData database
	 * @throws Exception if there is a problem in database connection or when retrieving pool connections
	 */
	public DataAccessor () throws Exception {

		try {
			Context initialContext = new InitialContext();
			if (initialContext == null){
				System.err.println("Failed to get initial context");
				throw new Exception("Failed to get initial context");
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
					throw new ConnectException("Failed to get datasource");
				}
			}
		} catch (SQLException ex){
			throw new SQLException("Cannot get connection: " + ex);
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

	/**
	 * Returns a JSON object which contains all values of one sensor
	 *      depending on time (if time specified).
	 * If only beginning time is specified, the last value saved before this date is returned.
	 * If beginning and ending time are specified, the values between these two dates are returned.
	 * If there is no date specified, all values of the sensor are returned.
	 *
	 * @param idSensor ID sensor that we want to retrieve values
	 * @param beg timestamp of beginning date
	 * @param end timestamp of ending date
	 * @return a JSON Object with the values of the sensor, depending on time.
	 * @throws SQLException if there is a problem with the database connection.
	 */
	public String getDataFromSensor (String idSensor, long beg, long end) throws SQLException {

		/* SQL Statement */
		String selectSQL = "SELECT * FROM \"public\".\"SensorsData\" WHERE sensor_id = ?";

		if (beg != 0L) {
			if (end != 0L) {
				selectSQL += " AND sensor_date >= ? AND sensor_date <= ?";
			} else {
				selectSQL += " AND sensor_date = ";
				selectSQL += "                  (SELECT MAX(sensor_date) FROM \"public\".\"SensorsData\" ";
				selectSQL += "                      WHERE sensor_id = ? AND sensor_date <= ?)";
			}
		}

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();

		try {
			ps = connection.prepareStatement(selectSQL);

			/* Parameters for SQL Statement */
			ps.setString(1, idSensor);
			if (beg != 0L) {
				if (end != 0L) {
					ps.setString(2, String.valueOf(beg));
					ps.setString(3, String.valueOf(end));
				} else {
					ps.setString(2, idSensor);
					ps.setString(3, String.valueOf(beg));
				}
			}

			rs = ps.executeQuery();

			/* Retrieving results in JSON Array */
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("date", rs.getString("sensor_date"));
				obj.put("value", rs.getString("sensor_value"));
				jsonArray.put(obj);
			}
			ps.close();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new SQLException(e);
		}

		/* Returned JSON Object */
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
