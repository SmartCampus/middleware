import http.client


class DiscoveryProtocol:
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
            self.connectingToBridge = http.client.HTTPConnection(ipAdress,port,timeout=8)
        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def discoverRequest(self):
        """
        Method to retrieve the board connected to a bridge at a given URL

        Return:
        BoardsJson -- Json Object containing the list of all Boards connected to the Bridge
        """
        try:
            self.connectingToBridge.request("GET", "/boards")
            responseBoards = self.connectingToBridge.getresponse()
            if responseBoards.status == http.client.OK:
                BoardsName = responseBoards.read().decode()
                return BoardsName
            else:
                raise Exception('An error occured the sever send',responseBoards.status,responseBoards.reason)
        except Exception as exception:
            print(type(exception))
            print(exception.args)


    def putConfig(self,confInJson):
        """
        Methode to push Json conf of the sensors to the bridge

        Return the result of the PUT request
        """
        #TODO test this method
        try:
            self.connectingToBridge.request("PUT","/config",confInJson)
            responseConf = self.connectingToBridge.getresponse()
            status = responseConf.status
            if status == http.client.ACCEPTED or status == http.client.OK or status == http.client.NO_CONTENT:
                print(status)
                print(responseConf.reason)
                print(responseConf.read().decode())
            else:
                raise Exception('An error occured the sever send',responseConf.status,responseConf.reason)

        except Exception as exception:
            print(type(exception))
            print(exception.args)


def main():
    """
    methode main
    testing purpose
    """
    Discover = DiscoveryProtocol("home.cecchinel.fr","9001")

    h1 = Discover.discoverRequest()
    print(h1)

if __name__ == '__main__':
    main()
