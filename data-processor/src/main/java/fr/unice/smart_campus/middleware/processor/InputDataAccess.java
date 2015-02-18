package fr.unice.smart_campus.middleware.processor;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


/**
 * InputDataAccess class
 * Interface to retrieve sensor values from the message queue
 */
public class InputDataAccess {

	private final static String DESTINATION_URL = "tcp://localhost:61616";
	private final static String QUEUE_NAME      = "sensor-values-queue";


	/**
	 * Create a message consumer that call the given message listener each time a sensor message is received
	 */
	public static void createMessageListener (MessageListener messageListener) throws JMSException {

		ConnectionFactory factory = new ActiveMQConnectionFactory(DESTINATION_URL);
		Connection connection = factory.createConnection();
		connection.start();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(messageListener);
	}
}
