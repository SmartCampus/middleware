package fr.unice.smart_campus.middleware.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


/**
 * OutputDataAccess class
 * Interface to retrieve sensor values from the message queue
 */
public class OutputDataAccess {

	private Connection connection;


	/**
	 * Create a new connection to the database
	 */
	public OutputDataAccess() throws Exception {
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));

		String connectionStr = "jdbc:postgresql://" + properties.get("hostname") + ":" + properties.get("port")
				+ "/" + properties.get("dbname");

		connection = DriverManager.getConnection(
				connectionStr,
				(String) properties.get("username"),
				(String) properties.get("password"));
	}


	/**
	 * Save a sensor data into the database
	 *
	 * @param name The sensor name
	 * @param time The data date
	 * @param value The data value
	 */
	//TODO: Handle database errors and restore the message in the message queue to avoid loosing messages
	public boolean saveSensorData (String name, String time, String value) {

		String sql = "INSERT INTO \"public\".\"SensorsData\" (sensor_id, sensor_date, sensor_value) VALUES (?,?,?)";

		int nb;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, time);
			ps.setString(3, value);
			nb = ps.executeUpdate();
		} catch (SQLException exc) {
			exc.printStackTrace();
			return false;
		}

		return (nb == 1);
	}
}
