package fr.unice.smart_campus.middleware.dataAPI.resources;


import fr.unice.smart_campus.middleware.dataAccessor.App;

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
        return null;
	}


	@GET
	@Produces("text/plain")
	public Response getValue () {
        App test = new App();

        return Response
                .status(Status.ACCEPTED)
                .entity(test.test())
                .build();
	}


	@PUT
	@Produces("text/plain")
	public Response putValue () {
        return null;
	}

}
