package fr.unice.smart_campus.middleware.resources;

import fr.unice.smart_campus.middleware.accessor.DataAccessor;
import fr.unice.smart_campus.middleware.Helper;
import fr.unice.smart_campus.middleware.TimeRange;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * GET requiest to retrieve the last value of one sensor (id)
     * @param idSensor Identifier of the sensor
     * @param convert Convert timestamp to human-readable date
     * @return a JSON Object with the last value of the sensor
     *          (or 500 HTTP error if the connection to the database does not work
     *          or 400 HTTP error if the url is not correct)
     */
    @GET
    @Path("{idSensor}/data/last")
    public Response getLastDataFromSensor(@PathParam("idSensor") String idSensor,
                                          @QueryParam("convert") boolean convert,
                                          @QueryParam("format") int format)
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);

        return getDataFromSensor(idSensor, strDate, convert, format);
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
	public Response getDataFromSensor (@PathParam("idSensor") String idSensor,
	                                   @QueryParam("date") String date,
                                       @QueryParam("convert") boolean convert,
                                       @QueryParam("format") int format) {

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
			data = access.getDataFromSensor(idSensor, time.getFirst(), time.getSecond(), convert, format);
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
