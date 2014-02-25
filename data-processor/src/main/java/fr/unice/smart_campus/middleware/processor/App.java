package fr.unice.smart_campus.middleware.processor;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * Message processor application
 * Consumes messages from the message queue, process data and store raw sensor data into the database
 */
public class App implements MessageListener {

	public static void main (String[] args) throws JMSException {
		DataAccess.createMessageListener(new App());
	}

	@Override
	public void onMessage (Message message) {
		if (message instanceof TextMessage) {
			TextMessage jsonMessage = (TextMessage) message;
			try {
				System.out.println(jsonMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
