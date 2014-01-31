import re
import postgresql.driver as pgdriver
import Config.config_sensor.JsonParsing as jsp


class DatabaseConnection:
    """
    class that implements the Database Connection and allow custom resquest

    Parameter:
    dbconnection -- Database connection object

    Available Method:
    fetchBridgeData(name)
    fetchSensorConfig(boardId)
    """
    def __init__(self,pathToDBFile):
        """
        Constructor of the class

        Keywords Arguments:
        pathToDBFile -- location of the file containing all informations needed to connect to the database
        """
        try:
            self.dbconnection = self.connectToDatabase(self.retrieveDbInformation(pathToDBFile))

        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def retrieveDbInformation(self,Pathfile):
        """
        Method to retrieve the database adress and the credentials to access it
        they are stored in a .txt file
        Text file must have the following format:
        login \n
        password \n
        IP or url \n
        Port \n
        base name \n

        Keyword Argument:
        Pathfile -- Path to access the file

        Return a list containing the login and the password
        """
        with open(Pathfile,"r",encoding='utf-8') as f:
        #lecture du fichier
            data = f.read()
            return re.split("\n", data)

    def connectToDatabase(self,dbConf):
        """
        Method to connect to the database

        Keyword Argument:
        dbConf -- list containing ( user password ip port base )

        Return a Connection Object
        """
        db = pgdriver.connect(
            user = dbConf[0],
            password = dbConf[1],
            host = dbConf[2],
            port = dbConf[3],
            database = dbConf[4]
        )
        return db

    def fetchBridgeData(self, name):
        """
        Method to retrieve from the database the Ip address and port number of
        a bridge

        Keyword Argument
        name -- id of the bridge

        Return a list containing the ip (or URL) and the port number of the bridge
        """
        try:
            ps = self.dbconnection.prepare("Select ip,port \
                          from bridge \
                          where idbridge = $1")
            return ps(name)[0]

        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def fetchSensorConfig(self, boardId):
        """
        Method to retrieve from the database the configuration of all sensor of a board

        Keyword Argument
        boardId -- id of the board

        Return a list sensor's configuration
        a configuration contains:
        - id of the sensor
        - id of the board
        - pin of the sensor on the board
        - frequency of the measure
        - Ip adress of the collector
        - port number of the collector
        """
        try:
            ps = self.dbconnection.prepare("Select c.idcapteur,p.idboard,p.boardpin,c.frequency,p.endpointIP,p.port \
                          from capteur c, capteurphy p \
                          where p.idboard = $1 and p.idcapteur = c.idcapteur")
            return ps(boardId)
        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def fetchAllBridge(self):
        """

        """
        try:
            ps = self.dbconnection.prepare("Select idbridge from bridge")
            return ps()
        except Exception as exception:
            print(type(exception))
            print(exception.args)

def main():
    """
    methode main
    testing purpose
    """
    #TODO more sensors, more boards on a same bridge
    clas = DatabaseConnection("../sql_database/user.txt")
    print(clas.fetchBridgeData("pi-1"))
    print(clas.fetchSensorConfig("NEWTON_B332_B2"))
    test =jsp.JsonConfigGroup(clas.fetchSensorConfig("NEWTON_B332_B2"))
    print(test)


if __name__ == '__main__':
    main()
