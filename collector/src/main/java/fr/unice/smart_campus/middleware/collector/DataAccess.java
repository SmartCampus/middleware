package fr.unice.smart_campus.middleware.collector;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;

import javax.jms.*;


/**
 * DataAccess class
 * Interface to post sensor values into the message queue
 */
public class DataAccess {

	private final static String DESTINATION_URL = "tcp://localhost:61616";
	private final static String QUEUE_NAME      = "sensor-values-queue";

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
	 */
	private DataAccess () throws JMSException {

		ConnectionFactory factory = new ActiveMQConnectionFactory(DESTINATION_URL);
		Connection connection = factory.createConnection();
		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(QUEUE_NAME);
		producer = session.createProducer(destination);
	}


	/**
	 * Post a sensor message into the message queue
	 *
	 * @param json The JSON object that contains sensor data
	 * @return true if data insert is successful, false otherwise
	 */
	public boolean postMessage (JSONObject json) {
		try {
			Message message = session.createTextMessage(json.toString());
			producer.send(message);
		} catch (JMSException exc) {
			exc.printStackTrace();
			return false;
		}

		return true;
	}
}
