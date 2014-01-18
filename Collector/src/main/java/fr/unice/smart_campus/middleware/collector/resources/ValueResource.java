package fr.unice.smart_campus.middleware.collector.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("value")
public class ValueResource {

	@GET
	@Produces("text/plain")
	public String getMessage () {
		return "Hello World!";
	}
}
