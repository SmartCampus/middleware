package fr.unice.smart_campus.middleware.database;

/*
 * #%L
 * database
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


/**
 * OutputDataAccess class
 * Interface to retrieve sensor values from the message queue
 */
public class SensorsDataOutputDataAccess {

	private Connection connection;

	/**
	 * Create a new connection to the database
	 */
	public SensorsDataOutputDataAccess() throws Exception {
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
