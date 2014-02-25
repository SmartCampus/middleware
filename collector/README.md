# Collector module

The collector goal is to provide a scalable entry point into the middleware for sensors data. The collector also stores sensors data into the message queue.

## How to run it

Run the Maven command `mvn jetty:run` to start the collector server.

*NB: Before doing so, do not forget to run `mvn install` into the parent root folder to install the middleware.*