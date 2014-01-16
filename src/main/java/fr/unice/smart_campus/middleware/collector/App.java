package fr.unice.smart_campus.middleware.collector;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;


/**
 * Test application
 */
public class App extends ServerResource {

	public static void main (String[] args) throws Exception {
		new Server(Protocol.HTTP, 8080, App.class).start();
	}

	@Get
	public String getString () {
		return "Hello Middleware!";
	}
}
