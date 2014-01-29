import json
from collections import OrderedDict


def JsonConfigEncoding(config):
    """

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

    """
    listconf = []
    for i in configarray:
        listconf.append(JsonConfigEncoding(i))
    conf = {'config_sensors' : listconf}
    return json.JSONEncoder().encode(conf)

