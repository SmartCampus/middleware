Install SmartCampus on a new Ubuntu server
==========================================

In this server, you have to install : 
* java
* maven
* git
* python
* python-pip
* tornado
* eve
* mongodb
* postgresql


Download and install packages
----------------------------

```
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get update
sudo apt-get install -y oracle-java8-installer
sudo apt-get install -y oracle-java8-set-default

sudo apt-get install -y maven
sudo apt-get install -y git

sudo apt-get install -y python3
sudo apt-get install -y python-pip
sudo pip install tornado
sudo pip install eve

sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
sudo apt-get update
sudo apt-get install -y mongodb-org

sudo apt-get install -y postgresql
```

Retrieve middleware from git
----------------------------------

```
git clone https://github.com/SmartCampus/middleware.git ./SmartCampus
```

Configure postgresql database
-----------------------------
```
#! Postgresql database
cd database
sudo -u postgres psql postgres < ./SensorsData.sql
cd ..
```

Configure mongo database
-------------------------
First, enter "mongo" in your command line, then use the command "use <DatabaseName>" in order to create your database. You can use the name "ConfigDatabase" which is located in setting.py.
Second, you must modify run.py and add your certificates or remove them (line 23) :
```
ssl_options={
				"certfile":  "server.crt",
				"keyfile": "server.key",
			})
```

In settings.py, you need to change the name of the server (Use IP or public DNS address, you can't use localhost here) and the database name (if you didn't use "ConfigDatabase")



Scripts
=======

Message Queue
-------------
```
cd ..
git reset --hard HEAD
git pull origin develop-PFE2015
#! We have to configure run.py, settings.py and data/-api/pom.xml  
#! So, we saved them in an extern directory and we copy them in this script.
#! cp ../savedFiles/run.py config/ConfigAPI/run.py
#! cp ../savedFiles/settings.py config/ConfigAPI/settings.py
#! cp ../savedFiles/pom.xml data/-api/pom.xml
mvn clean install
cd message-queue
mvn activemq:run
```

Collector
---------
```
cd ../collector
mvn jetty:run
```

Data processor
--------------
```
cd ../data-processor
mvn exec:java
```

Data Api
--------
```
cd ../data-api
mvn jetty:run
```

Config
------
```
cd ../config/ConfigAPI
sudo service mongod restart
python run.py
```


Restart the server
=================
There are several ways to connect to a Linux instance and restart your services. Choose the method that meets your needs:
* Connect from Linux Using an SSH Client
* Connect from Windows Using PuTTY

Then you have to run the script SmartCampus-Start.sh

Connect from Linux Using an SSH Client
-------------------
```
gnome-terminal --title "SmartCampus : VM" -x bash -c "ssh -t -i </path/key_pair.pem> <ec2-user@public_dns_name> './SmartCampus/scripts/SmartCampus-Start.sh; bash'"
```

Connect from Windows Using PuTTY
-------------------
In putty, you can give the remote command that you want to execute on the server : 
```
./SmartCampus/scripts/SmartCampus-Start.sh; bash
```
