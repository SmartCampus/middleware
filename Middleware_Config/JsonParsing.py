__author__ = 'matthieujimenez'
import json
def JsonConfigEncoding(config):
    conf=[]
    conf['id']=config[0]
    conf['board']=config[1]
    conf['pin']=config[2]
    conf['freq']=config[3]
    conf['endpointIP']=config[4]
    conf['endpointPort']=config[5]
    return json.JSONEncoder(conf)

def JsonConfigGroup(configarray):
    conf=[]
    conf['config_sensors']=configarray
    return json.JSONEncoder(conf)

