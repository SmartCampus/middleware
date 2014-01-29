import re

import postgresql.driver as pgdriver

import Config.Middleware_Config.JsonParsing as jsp


def retrieveUserMdp(Pathfile):
    """
    Method to retrieve the credentials to access the database
    they are stored in a .txt file (login first line and password second)

    Keyword Argument:
    Pathfile -- Path to access the file

    Return a list containing the login and the password
    """
    with open(Pathfile,"r",encoding='utf-8') as f:
    #lecture du fichier
        data = f.read()
        return re.split("\n",data)


def connectToDatabase(user,ip,port,base):
    """
    Method to connect to the database

    Keyword Argument:
    user -- list containing the login and the password
    ip -- Ip or URL address of the database
    port -- port (usually 5432)
    base -- name of the database to access

    Return a Connection Object
    """
    db = pgdriver.connect(
        user = user[0],
        password = user[1],
        host = ip,
        port = port,
        database = base
    )
    return db



def fetchBridgeData(name,db):
    """
    Method to retrieve from the database the Ip address and port number of
    a bridge

    Keyword Argument
    name -- id of the bridge
    db -- Database Connection object

    Return a list containing the ip (or URL) and the port number of the bridge
    """
    #TODO exception
    ps = db.prepare("Select ip,port \
                  from bridge \
                  where idbridge = $1")
    return ps(name)[0]


def fetchSensorConfig(boardId,db):
    """
    Method to retrieve from the database the configuration of all sensor of a board

    Keyword Argument
    boardId -- id of the board
    db -- Database Connection object

    Return a list sensor's configuration
    a configuration contains:
    - id of the sensor
    - id of the board
    - pin of the sensor on the board
    - frequency of the measure
    - Ip adress of the collector
    - port number of the collector
    """
    #TODO exception
    ps = db.prepare("Select c.idcapteur,p.idboard,p.boardpin,c.frequency,p.endpointIP,p.port \
                  from capteur c, capteurphy p \
                  where p.idboard = $1 and p.idcapteur = c.idcapteur")
    return ps(boardId)


def main():
    """
    methode main
    testing purpose
    """
    #TODO more sensors, more boards on a same bridge
    user = retrieveUserMdp("../sql_database/user.txt")
    db = connectToDatabase(user,"127.0.0.1","5432","smartcampusconfig")
    print(fetchBridgeData("pi-1",db))
    print(fetchSensorConfig("ard-101",db))
    test =jsp.JsonConfigGroup(fetchSensorConfig("ard-101",db))
    print(test)


if __name__ == '__main__':
    main()
