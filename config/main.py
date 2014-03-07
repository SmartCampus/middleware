__author__ = 'matthieujimenez'
"""
"""
import multiprocessing
import time
from config_sensor import scriptbridge as SB
import config_sensor.SensorApi as SA


def configsensor(ip,port):
    """
    Method to propagate a new config to every bridge present in the database
    """
    api = SA.SensorApi(ip,port)
    listbridge = api.fetchAllBridge()
    listproc = []
    encours = True
    for i in listbridge:
       pi = multiprocessing.Process(target=SB.scriptbridge, args=(ip,port, i))
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
    configsensor("smartcampus.jimenez.lu","5000")