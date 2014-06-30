package fr.unice.smart_campus.middleware.data_api.resources;

import fr.unice.smart_campus.middleware.accessor.DataAccessor;
import fr.unice.smart_campus.middleware.data_api.Helper;
import fr.unice.smart_campus.middleware.data_api.TimeRange;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;

import static javax.ws.rs.core.Response.Status;


/**
 * Sensor REST resource.
 * To get details and values of sensors.
 */
@Path("sensors")
public class SensorResource {

	@GET
	@Produces("application/json")
	public Response getSensors () {
		DataAccessor access = null;
		String data = "";

		try {
			// Try to get access to the database
			access = new DataAccessor();
			data = access.getSensors(null);
			access.close();
		} catch (Exception e) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Error: " + e.getMessage())
					.build();
		}

		return Response
				.status(Status.ACCEPTED)
				.entity(data)
				.build();
	}


	/**
	 * GET request to retrieve values of one sensor (id)
	 *      depending on dates :
	 *      - no dates : all values of the sensor are returned
	 *      - one date : the last value of the sensor before this date
	 *      - two dates : al values before these two dates (included)
	 *
	 * @param idSensor Identifier of the sensor
	 * @param date Dates of recorded values that you want. Optional.
	 *             Format : yyyy-MM-dd HH:mm:ss or yyyy-MM-dd HH:mm:ss/yyyy-MM-dd HH:mm:ss
     * @param convert Convert timestamp to human-readable date
	 * @return a JSON Object with the values of the sensor, depending on time.
	 *          (or 500 HTTP error if the connection to the database does not work
	 *          or 400 HTTP error if the url is not correct)
	 */
	@GET
	@Path("{idSensor}/data")
	@Produces("application/json")
	public Response getDataFromSensor (@PathParam("idSensor") String idSensor,
	                                   @QueryParam("date") String date,
                                       @QueryParam("convert") boolean convert) {

		// URL Dates parsing
		TimeRange time = new TimeRange(0L, 0L);
		if (date != null) {
			try {
				time = Helper.getTimestamps(date);
			} catch (ParseException exc) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(exc.getMessage())
						.build();
			}
		}

		DataAccessor access = null;
		String data = "";
		String error = null;
		try {
			// Try to get access to the database
			access = new DataAccessor();
			data = access.getDataFromSensor(idSensor, time.getFirst(), time.getSecond(), convert);
			access.close();
		} catch (Exception e) {
			access.close();
			error = e.getMessage();
		}

		if (error != null)
			return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Error: " + error)
				.build();

		return Response
				.status(Status.ACCEPTED)
				.entity(data)
				.build();
	}

	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public Response postValue (String jsonString) {
		return null;
	}

	@PUT
	@Produces("text/plain")
	public Response putValue (String jsonString) {
		return null;
	}
}
