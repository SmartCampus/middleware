package fr.unice.smart_campus.middleware.processor;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * Message processor application
 * Consumes messages from the message queue, process data and store raw sensor data into the database
 */
public class App implements MessageListener {

	private OutputDataAccess outputDataAccess;


	public App () throws Exception {

		// Create output proxy
		outputDataAccess = new OutputDataAccess();

		// Create input listener
		InputDataAccess.createMessageListener(this);
	}


	/**
	 * Executed when a message is received from the message queue
	 * @param message The sensor data message
	 */
	@Override
	public void onMessage (Message message) {
		if (message instanceof TextMessage) {
			try {
				String jsonString = ((TextMessage) message).getText();

				// TODO: TEMP: Print received JSON message
				System.out.println(jsonString);

				// Extract sensor information
				JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
				String name  = jsonObject.getString("n");
				String time  = jsonObject.getString("t");
				String value = jsonObject.getString("v");

				// TODO: Make the value pass through multiples processors to process it

				// Save sensor data into the database
				outputDataAccess.saveSensorData(name, time, value);

			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}


	public static void main (String[] args) throws Exception {
		new App();
	}
}
