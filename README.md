# SmartCampus Middleware 

The middleware is the core system of the SmartCampus architecture. It is composed by different kinds of modules:

- **Collector**, to collect sensors data ;
- **Data Processor**, to process sensors data from the message queue ;
- **Database**, to store and access to sensors data ;
- **CEP Engine**, to detect pattern on sensors and trigger events;
- **Virtual Sensor Computing**, to compute virtual sensors values ;
- **Data API**, to retrieve physical and virtual sensors data ;
- **Config**, to easily configure the sensors network ;
- **Sensors API**, to manage sensors configurations ;


## How to install it

Before running the modules, you have to install the middleware by running the Maven command `mvn install` in this directory. Then, to make the middleware working, start the modules in the following order:


1. Collector
2. Data Processor
3. Database
4. CEP Engine
5. Virtual Sensor Computing
6. Data API
8. Sensors API
8. Config


# Testing

An integration test on `integration.xml` is wrote with SoapUI. You can import it to launch on your SmartCampus deployment or launch it with maven : `mvn com.smartbear.soapui:soapui-maven-plugin:4.6.1:test`

*NB: See the README of each module for documentation.*

You can also use the scripts located in ./scripts to deploy the solution
