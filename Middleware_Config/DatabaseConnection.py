

import postgresql.driver as pgdriver
import re
def retrieveUserMdp(Pathfile):
    '''

    '''
    with open(Pathfile,"r",encoding='utf-8') as f:
    #lecture du fichier
        data = f.read()
        return re.split("\n",data)

def connectToDatabase(user,ip,port,base):
    '''

    '''
    db=pgdriver.connect(
        user = user[0],
        password = user[1],
        host = ip,
        port = port,
        database = base
    )
    return db

def fetchBridgeData(name,db):
    '''

    '''
    ps=db.prepare("Select ip,port \
                  from bridge \
                  where idbridge = $1")
    return ps(name)[0]

def fetchSensorConfig(boardId,db):
    ps=db.prepare("Select c.idcapteur,p.idboard,p.boardpin,c.frequency,p.endpointIP,p.port \
                  from capteur c, capteurphy p \
                  where p.idboard = $1 and p.idcapteur = c.idcapteur")
    return ps(boardId)[0]

def main():
    """
    methode main
    """
    user= retrieveUserMdp("../sql_database/user.txt")
    db=connectToDatabase(user,"127.0.0.1","5432","smartcampusconfig")
    print(fetchBridgeData("pi-1",db))
    print(fetchSensorConfig("ard-101",db))

if __name__ == '__main__':
    main()
