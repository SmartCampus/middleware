package fr.unice.smart_campus.middleware.dataAPI.resources;


import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status;


/**
 * Sensor REST resource
 */
@Path("sensor")
public class SensorResource {

	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public Response postValue (String jsonString) {
        return Response
                .status(Status.ACCEPTED)
                .entity("Hello World!")
                .build();
	}


	@GET
	@Produces("text/plain")
	public Response getValue () {
		return null;
	}


	@PUT
	@Produces("text/plain")
	public Response putValue () {
        return null;
	}

}
