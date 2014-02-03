__author__ = 'matthieujimenez'
"""
"""
import multiprocessing
import time
import Config.database.DatabaseConnection as DC
import Config.config_sensor.scriptbridge as SB


def configsensor(pathToDBFile):
    """
    Method to propagate a new config to every bridge present in the database
    """
    db = DC.DatabaseConnection(pathToDBFile)
    listbridge = db.fetchAllBridge()
    listproc = []
    encours = True
    for i in listbridge:
       pi = multiprocessing.Process(target=SB.scriptbridge, args=(pathToDBFile, i))
       pi.start()
       time.sleep(5)
       listproc.append(pi)
    while  encours :
        temp = False
        for i in listproc:
             temp = temp or i.is_alive()
        encours = temp

    print("Fin du processus")


if __name__ == '__main__':
    configsensor("database/user.txt")