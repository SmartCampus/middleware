package fr.unice.smart_campus.middleware.collector;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jettison.json.JSONObject;

import javax.jms.*;

/**
 * DataAccess class
 * Interface to post sensor values into the middleware
 */
public class DataAccess {

	private final static String brokerURL = "tcp://localhost:61616";

	private static DataAccess instance;
	private Session session;
	private MessageProducer producer;


	/**
	 * Get the class instance
	 */
	public static synchronized DataAccess getInstance () throws JMSException {
		if (instance == null) {
			instance = new DataAccess();
		}
		return instance;
	}

	/**
	 * Private constructor
	 * Get connection from data source (defined in jetty-env.xml)
	 */
	private DataAccess () throws JMSException {

		ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
		Connection connection = factory.createConnection();
		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("svalues");
		producer = session.createProducer(destination);
	}

	/**
	 * Post a sensor value in the MQ
	 *
	 * @param name  The sensor identifier
	 * @param time  The timestamp
	 * @param value The sensor value
	 * @return true if data insert is successful, false otherwise
	 */
	public boolean postValue (String name, String time, String value) {
		try {
			JSONObject json = new JSONObject();
			json.put("name", name);
			json.put("time", time);
			json.put("value", value);

			Message message = session.createTextMessage(json.toString());
			producer.send(message);

		} catch (Exception exc) {
			return false;
		}

		return true;
	}
}
