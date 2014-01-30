"""
Main script
"""
from Config.Middleware_Config import DiscoveryProtocol as DP
from Config.Middleware_Config import DatabaseConnection as DC
from Config.Middleware_Config import JsonParsing as JP


def main(pathToDBConfFile,bridgeID):
    db = DC.DatabaseConnection(pathToDBConfFile)
    bridgeInfo = db.fetchBridgeData(bridgeID)
    print(bridgeInfo)
    bridgeconnection = DP.DiscoveryProtocol(bridgeInfo[0],bridgeInfo[1])
    jsonboard = bridgeconnection.discoverRequest()
    listboard = JP.JsonBoardsDecode(jsonboard)
    listconfig = []
    for i in listboard:
       listconfig = listconfig + (db.fetchSensorConfig(i))
    jsonConf = JP.JsonConfigGroup(listconfig)
    bridgeconnection.putConfig(jsonConf)

if __name__ == '__main__':
    main("../sql_database/user.txt","pi-1")
