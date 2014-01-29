import json
from collections import OrderedDict


def JsonConfigEncoding(config):
    """
    Method to transform a list of the configuration of a sensor
    in Json Format
    """
    conf = OrderedDict()
    conf['id'] = config[0]
    conf['board']= config[1]
    conf['pin'] = config[2]
    conf['freq'] = config[3]
    conf['endpointIP'] = config[4]
    conf['endpointPort'] = config[5]
    return json.JSONEncoder().encode(conf)


def JsonConfigGroup(configarray):
    """
    Method to transform the result of an sql config request in Json
    Format
    """
    listconf = []
    for i in configarray:
        listconf.append(JsonConfigEncoding(i))
    conf = {'config_sensors' : listconf}
    return json.JSONEncoder().encode(conf)

