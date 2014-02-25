# Message Queue module

The message queue goal is to provide a scalable and persistent messaging system to store pending sensors data that come from the collector.

## How to run it

Run the Maven command `mvn activemq:run` to start the message queue server.

*NB: Before doing so, do not forget to run `mvn install` into the parent root folder to install the middleware.*