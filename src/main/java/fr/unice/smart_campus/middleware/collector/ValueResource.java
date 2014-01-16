package fr.unice.smart_campus.middleware.collector;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


/**
 * The value resource.
 * Represents a sensor value
 */
public class ValueResource extends ServerResource {

	@Get
	public String getString () {
		String name = (String) getRequestAttributes().get("name");
		return "Hello " + name;
	}
}
