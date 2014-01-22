package fr.unice.smart_campus.middleware.collector.resources;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;

import static javax.ws.rs.core.Response.Status;


@Path("value")
public class ValueResource {

	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public Response postValue (String jsonString) {

		String errorMessage = null;

		try {
			JSONObject json = new JSONObject(new JSONTokener(jsonString));

			String name = json.getString("n");
			String value = json.getString("v");
			String time = json.getString("t");

			Date date = new Date(Long.parseLong(time) * 1000);

			// TODO: TEMP: Display values
			System.out.println(
				"Received: name = " + name + ", value = " + value + ", time = " + time + " (" + date + ");");

		} catch (JSONException exc) {
			errorMessage = exc.getMessage();
		} catch (NumberFormatException exc) {
			errorMessage = "Wrong timestamp format";
		}

		if (errorMessage != null) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Error: " + errorMessage)
					.build();
		}

		return Response
				.status(Status.CREATED)
				.entity("Value posted with success!")
				.build();
	}


	@GET
	@Produces("text/plain")
	public Response getValue () {
		return getWrongCommandErrorResponse();
	}


	@PUT
	@Produces("text/plain")
	public Response putValue () {
		return getWrongCommandErrorResponse();
	}


	private static Response getWrongCommandErrorResponse () {
		return Response
				.status(Status.NOT_FOUND)
				.entity("Error: Use the POST HTTP command to post a value into the collector")
				.build();
	}
}
