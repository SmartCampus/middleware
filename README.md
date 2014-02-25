# SmartCampus Middleware 

The middleware is the core system of the SmartCampus architecture. It is composed by different kinds of modules:

- **Collector**, to collect sensors data ;
- **Message Queue**, to store pending sensors data ;
- **Data Processor**, to process sensors data from the message queue ;
- **Data Accessor**, to abstract physical and virtual sensors recovery ;
- **Data API**, to retrieve sensors data ;
- **Config**, to easily configure the sensors network ;
- **Sensors API**, to manage sensors configurations ;
- (The sensors database that contains sensors data and sensors configurations).

## How to install it

Before running the modules, you have to install the middleware by running the Maven command `mvn install` in this directory. Then, to make the middleware working, start the modules in the following order:

1. (Sensors database)
2. Message Queue
3. Collector
4. Data Processor
5. Data API
6. Sensors API
7. Config

*NB: See the README of each module for documentation.*