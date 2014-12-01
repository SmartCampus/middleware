cd ..
#! We have to configure run.py, settings.py and data/-api/pom.xml  
#! So, we saved them in a extern directory and we copy them in this script.
#! cp ../savedFiles/run.py config/ConfigAPI/run.py
#! cp ../savedFiles/settings.py config/ConfigAPI/settings.py
#! cp ../savedFiles/pom.xml data/-api/pom.xml
mvn clean install
cd message-queue
mvn activemq:run