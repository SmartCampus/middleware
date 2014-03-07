__author__ = 'matthieujimenez'

import http.client
import json

class SensorApi:
    """
    class that implement the discovery Protocol

    Parameters:
    connectingToBridge -- it's an HTTPConnection object containing the adress of the bridge to reach

    Available Method:
    discoverRequest()
    putConfig(confInJson)
    """
    def __init__(self,ipAdress,port):
        """
        Constructor of the Discovery Protocol class

        Keywords Arguments:
        ipAdress -- IP adress if the bridge to reach
        port -- Port number of the REST interface of the bridge
        """
        try:
            self.connectingToApi = http.client.HTTPSConnection(ipAdress,port,timeout=8)
        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def fetchBridgeData(self, name):
        """
        Method to retrieve from the database the Ip address and port number of
        a bridge

        Keyword Argument
        name -- id of the bridge

        Return a list containing the ip (or URL) and the port number of the bridge
        """
        try:
            self.connectingToApi.request("GET", "/bridges/"+name)
            response = self.connectingToApi.getresponse()
            if response.status == http.client.OK:
                bridgeInfo = response.read().decode()
                bridgeInfojson = json.loads(bridgeInfo)
                return bridgeInfojson["adress"]
            else:
                raise Exception('An error occured the sever send',response.status,response.reason)
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
            self.connectingToApi.request("GET", "/sensors?where={\"board\":\""+boardId+"\"}")
            response = self.connectingToApi.getresponse()
            if response.status == http.client.OK:
                sensorInfo = response.read().decode()
                sensorInfojson = json.loads(sensorInfo)
                return sensorInfojson["_items"]
            else:
                raise Exception('An error occured the sever send',response.status,response.reason)
        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def fetchAllBridge(self):
        """
        Method to retrieve from the database a list of all the bridge that have been declared
        """
        try:
            self.connectingToApi.request("GET", "/bridges")
            {'_created': 'Wed, 05 Mar 2014 15:01:36 GMT', 'adress': {'ip': 'home.cecchinel.fr', 'port': 9001}, 'description': 'appartement de cyril', '_id': '53173c5066d04a049b69a346', '_updated': 'Wed, 05 Mar 2014 15:01:36 GMT', '_etag': 'b2c837e32d25cb6ccd63d3b98d97acf36dc672ea', 'name': 'pi-1', '_links': {'self': {'href': 'smartcampus.jimenez.lu:5000/bridges/53173c5066d04a049b69a346', 'title': 'bridge'}}}
            response = self.connectingToApi.getresponse()
            if response.status == http.client.OK:
                bridgeInfo = response.read().decode()
                bridgeInfojson = json.loads(bridgeInfo)
                listbridgefull = bridgeInfojson["_items"]
                listbridge = []
                for i in listbridgefull:
                    listbridge.append(i["name"])
                return listbridge

            else:
                raise Exception('An error occured the sever send',response.status,response.reason)
        except Exception as exception:
            print(type(exception))
            print(exception.args)




def main():
    """
    methode main
    testing purpose
    """
    Api = SensorApi("smartcampus.jimenez.lu","5000")

    h1 = Api.fetchBridgeData("pi1")
    print(h1)

    h2 = Api.fetchSensorConfig("SMART_CAMPUS_BOARD_2")
    print(h2)

    h3 = Api.fetchAllBridge()
    print(h3)


if __name__ == '__main__':
    main()
