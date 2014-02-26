package fr.unice.smart_campus.middleware.dataapi.resources;


import fr.unice.smart_campus.middleware.dataaccessor.DataAccessor;
import fr.unice.smart_campus.middleware.dataapi.Helper;
import fr.unice.smart_campus.middleware.dataapi.TimeRange;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;

import static javax.ws.rs.core.Response.Status;


/**
 * Sensor REST resource
 */
@Path("sensors")
public class SensorResource {

	@GET
    @Produces("application/json")
	public Response getSensors () {
        DataAccessor access = new DataAccessor();


        String sensors = access.getSensors();

        return Response
                .status(Status.ACCEPTED)
                .entity(sensors)
                .build();
	}

    @GET
    @Path("{idSensor}/data")
    @Produces("application/json")
    public Response getDataFromSensor (@PathParam("idSensor") String idSensor,
                                       @QueryParam("date") String date) {

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

        DataAccessor access = new DataAccessor();
        String data = access.getDataFromSensor(idSensor, time.getFirst(), time.getSecond());

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
