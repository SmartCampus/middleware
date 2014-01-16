package fr.unice.smart_campus.middleware.collector;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;


/**
 * Collector application main class.
 * The middleware collector listen for data from the bridge and store them into a message queue
 */
public class CollectorApp extends Application {

	/** The default collector port number */
	private static final int DEFAULT_PORT = 8080;


	/**
	 * Map resource URL to classes
	 */
	@Override
	public Restlet createRoot () {
		Router router = new Router(getContext());

		router.attach("/value/{name}", ValueResource.class);

		return router;
	}


	/**
	 * Collector entry point
	 * @param args Program arguments
	 * @throws Exception
	 */
	public static void main (String[] args) throws Exception {

		// Try to get the port
		int port = 0;
		if (args.length > 0) {
			try { port = Integer.parseInt(args[0]); }
			catch (NumberFormatException exc) {}
		}

		if (port <= 0) {
			port = DEFAULT_PORT;
		}

		System.out.println("Starting server on port " + port + "...");

		Component component = new Component();
		component.getServers().add(Protocol.HTTP, port);
		component.getDefaultHost().attach(new CollectorApp());
		component.start();

		System.out.println("Server started.");
	}
}
