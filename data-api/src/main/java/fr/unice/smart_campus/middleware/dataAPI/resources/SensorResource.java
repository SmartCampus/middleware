package fr.unice.smart_campus.middleware.dataapi.resources;


import fr.unice.smart_campus.middleware.dataaccessor.DataAccessor;
import fr.unice.smart_campus.middleware.dataapi.Helper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

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


        long timestampFirst = 0;
        long timestampSecond = 0;

        if (date != null) {
            Helper.getTimestamps(date);
        }

        DataAccessor access = new DataAccessor();
        String data = idSensor + " : " + access.getDataFromSensor(idSensor, timestampFirst, timestampSecond) + " : " + date;

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
