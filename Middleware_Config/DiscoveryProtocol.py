__author__ = 'matthieujimenez'
import http.client
import json

class DiscoveryProtocol:
    def __init__(self,ipAdress,port):
        self.connectingToBridge= http.client.HTTPConnection(ipAdress,port)

    def discoverRequest(self):
        '''
        Method to retrieve the board connected to a bridge at a given URL

        Keyword argument:
        connectingToBridge -- it's an HTTPConnection object containing the adress of the bridge to reach

        Return:
        BoardsJson -- Json Object conataing the list of all Boards connected to the Bridge
        '''
        try:
            self.connectingToBridge.request("GET","/boards")
            responseBoards = self.connectingToBridge.getresponse()
            if (responseBoards.status==http.client.OK):
                BoardsName=responseBoards.read().decode()
                BoardsJson=json.loads(BoardsName)
                return BoardsJson
            else:
                raise Exception('An error occured the sever send',responseBoards.status,responseBoards.reason)
        except Exception as exception:
            print(type(exception))
            print(exception.args)

    def putConfig(self,confInJson):
        '''

        '''
        try:
            self.connectingToBridge.request("PUT","/config",confInJson)
            responseConf= self.connectingToBridge.getresponse()
            status=responseConf.status
            if (status==http.client.ACCEPTED or status==http.client.OK or status==http.client.NO_CONTENT):
                print(status)
                print(responseConf.reason)
            else:
                raise Exception('An error occured the sever send',responseConf.status,responseConf.reason)

        except Exception as exception:
            print(type(exception))
            print(exception.args)



def main():
    """
    methode main
    """
    Discover=DiscoveryProtocol("home.cecchinel.fr","9001")

    h1=Discover.discoverRequest()
    print(h1)

if __name__ == '__main__':
    main()
#connectingToBridge = http.client.HTTPConnection(ip,port)