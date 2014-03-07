import json
from collections import OrderedDict


def JsonConfigEncoding(config):
    """
    Method to transform a list of configuration of a sensor
    in Json Format
    """
    conf = OrderedDict()
    conf['id'] = config['name']
    conf['board']= config['board']
    conf['pin'] = config['pin']
    conf['freq'] = str(config['frequency'])
    conf['endpointIP'] = config['endpoint']['ip']
    conf['endpointPort'] = str(config['endpoint']['port'])
    return conf


def JsonConfigGroup(configarray):
    """
    Method to aggregate in a Json all the configuration for many sensors in Json
    Format
    """
    listconf = []
    for i in configarray:
        listconf.append(JsonConfigEncoding(i))
    conf = {'config_sensors' : listconf}
    return json.JSONEncoder().encode(conf)


def JsonBoardsDecode(jsonboard):
    """
    Method to decode a Json object which contain a list of boards in a list
    """
    BoardsJson = json.loads(jsonboard)
    listj = BoardsJson["boards"]
    listf = []
    for i in listj:
        listf.append(i['id'])
    return listf
