package fr.unice.smart_campus.middleware.collector.resources;

import fr.unice.smart_campus.middleware.collector.DataAccess;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;

import static javax.ws.rs.core.Response.Status;


/**
 * Value REST resource
 * Used to post a value into the collector
 */
@Path("value")
public class ValueResource {

	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public Response postValue (String jsonString) {

		String errorMessage = null;
		int messagesCount = 0;

		try {

			JSONArray messages;
			boolean success = true;

			// Parse a JSON object or a JSON array
			try {
				JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
				messages = new JSONArray();
				messages.put(jsonObject);
			} catch (JSONException exc) {
				messages = new JSONArray(new JSONTokener(jsonString));
			}

			messagesCount = messages.length();

			// Process messages
			for (int i = 0; i < messagesCount; i++) {
				JSONObject message = (JSONObject) messages.get(i);

				String name  = message.getString("n");
				String value = message.getString("v");
				String time  = message.getString("t");

				// TODO: Temporarily display values
				Date date = new Date(Long.parseLong(time) * 1000);
				System.out.println(
						"Received: name = " + name + ", value = " + value + ", time = " + time + " (" + date + ");");

				// Store the message in the messages queue
				success = DataAccess.getInstance().postValue(name, time, value) && success;
			}

            if (!success) {
	            errorMessage = "Values not posted";
            }

		} catch (NumberFormatException exc) {
			errorMessage = "Wrong timestamp format";
		} catch (Exception exc) {
			errorMessage = exc.getMessage();
		}

		// HTTP response
		if (errorMessage != null) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Error: " + errorMessage)
					.build();
		}

		return Response
				.status(Status.CREATED)
				.entity(messagesCount + " value(s) posted with success!")
				.build();
	}


	@GET
	@Produces("text/plain")
	public Response getValue () {
		return getWrongMethodErrorResponse();
	}


	@PUT
	@Produces("text/plain")
	public Response putValue () {
		return getWrongMethodErrorResponse();
	}


	private static Response getWrongMethodErrorResponse () {
		return Response
				.status(Status.METHOD_NOT_ALLOWED)
				.entity("Error: Use the POST HTTP command to post a value into the collector")
				.build();
	}
}
