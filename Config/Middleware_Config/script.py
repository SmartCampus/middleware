"""
Main script
"""
#TODO script main
from Config.Middleware_Config import DiscoveryProtocol as DP
from Config.Middleware_Config import DatabaseConnection as DC


def main(pathToDBConfFile,bridgeID):
    db = DC.DatabaseConnection(pathToDBConfFile)
    bridgeInfo = db.fetchBridgeData(bridgeID)
    print(bridgeInfo)
    bridgeconnection = DP.DiscoveryProtocol(bridgeInfo[0],bridgeInfo[1])
    print(bridgeconnection.discoverRequest())


if __name__ == '__main__':
    main("../sql_database/user.txt","pi-1")
