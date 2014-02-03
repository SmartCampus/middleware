"""
Main script
"""
from Config.config_sensor import DiscoveryProtocol as DP
from Config.config_sensor import JsonParsing as JP
from Config.database import DatabaseConnection as DC


def scriptbridge(pathToDBConfFile,bridgeID):
    """
    script that regroup each calls needed to update the config list of a given board
    """
    try:
        db = DC.DatabaseConnection(pathToDBConfFile)
        bridgeInfo = db.fetchBridgeData(bridgeID)
        bridgeconnection = DP.DiscoveryProtocol(bridgeInfo[0],bridgeInfo[1])
        jsonboard = bridgeconnection.discoverRequest()
        listboard = JP.JsonBoardsDecode(jsonboard)
        listconfig = []
        for i in listboard:
           listconfig = listconfig + (db.fetchSensorConfig(i))
        jsonConf = JP.JsonConfigGroup(listconfig)
        bridgeconnection.putConfig(jsonConf)

    except Exception as exception:
            print ("an error occured with the bridge:" + bridgeID)
            print(type(exception))
            print(exception.args)

if __name__ == '__main__':
    scriptbridge("../sql_database/user.txt","pi-1")
