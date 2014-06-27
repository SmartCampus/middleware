"""
Main script
"""
import config_sensor.DiscoveryProtocol as DP
import config_sensor.JsonParsing as JP
import config_sensor.SensorApi as SA


def scriptbridge(ip,port,bridgeID):
    """
    script that regroup each calls needed to update the config list of a given board
    """
    try:
        api = SA.SensorApi(ip,port)
        print("Connecting To: "+ ip+":"+port)
        bridgeInfo = api.fetchBridgeData(bridgeID)
        print("Retrieving bridge "+bridgeID+" info:")
        print(bridgeInfo)
        bridgeconnection = DP.DiscoveryProtocol(bridgeInfo['ip'],bridgeInfo['port'])
        jsonboard = bridgeconnection.discoverRequest()
        listboard = JP.JsonBoardsDecode(jsonboard)
        print("Retrieving list of boards connected to "+bridgeID+":")
        print(listboard)
        listconfig = []
        for i in listboard:
           listconfig = listconfig + (api.fetchSensorConfig(i))
        jsonConf = JP.JsonConfigGroup(listconfig)
        print("Retrieving Configuration for sensors on this/these boards: ")
        print(jsonConf)
        bridgeconnection.putConfig(jsonConf)
        print("Configuration send to the bridge.")

    except Exception as exception:
          print ("an error occured with the bridge:" + bridgeID)
          print(type(exception))
          print(exception.args)


if __name__ == '__main__':
    scriptbridge("smartcampus.jimenez.lu","5000","pi1")
