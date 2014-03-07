__author__ = 'matthieujimenez'
sensorschema = {
    'name':{
        'type':'string',
        'minlength':1,
        'maxlength':15,
        'required':True,
        'unique':True
    },
    'description':{
        'type':'string'
    },
    'sensorType':{
        'type': 'string',
        'allowed': ["physical", "virtual"],
        'required':True
    },
    'frequency':{
        'type':'integer',
        'required':True
    },
    'board':{
        'type':'string'
    },
    'pin':{
      'type':'string'
    },
    'endpoint': {
        'type': 'dict',
        'schema': {
            'ip': {
                'type': 'string'
            },
            'port': {
                'type': 'integer',
                'allowed': range(65535)
            }
        }
    },
    'script':{
        'type':'string'
    },
    'tag':{
        'type':'list'
    },
    'kind':{
        'type':'string',
        'allowed':['temp','presence']
    }

}
bridgeschema = {
    'name':{
        'type':'string',
        'minlength':1,
        'maxlength':15,
        'required':True,
        'unique':True
    },
    'description':{
        'type':'string'
    },
    'adress':{
        'type': 'dict',
        'required':'true',
        'schema': {
            'ip': {
                'type': 'string'
            },
            'port': {
                'type': 'integer',
                'allowed': range(65535)
            }
        }
    },
}
passschema = {
    'username': {
        'type': 'string',
        'required': True,
        'unique': True,
        },
    'password': {
        'type': 'string',
        'required': True,
    },
    'roles': {
        'type': 'list',
        'allowed': ['user', 'admin'],
        'required': True,
    }
}

sensors = {

    'item_title': 'sensor',

    # by default the standard item entry point is defined as
    # '/people/<ObjectId>'. We leave it untouched, and we also enable an
    # additional read-only entry point. This way consumers can also perform
    # GET requests at '/people/<lastname>'.
    'additional_lookup': {
            'url': 'regex("[\w]+")',
        'field': 'name'
    },

    # We choose to override global cache-control directives for this resource.
    'cache_control': 'max-age=10,must-revalidate',
    'cache_expires': 10,

    # most global settings can be overridden at resource level
    #'item_methods': ['GET', 'POST','PATCH','DELETE'],
    'allowed_roles': ['user', 'admin'],
    'allowed_item_roles': ['user', 'admin'],
    'public_methods': ['GET'],
    'public_item_methods': ['GET'],
    'schema': sensorschema
}
bridges = {

    'item_title': 'bridge',

    # by default the standard item entry point is defined as
    # '/people/<ObjectId>'. We leave it untouched, and we also enable an
    # additional read-only entry point. This way consumers can also perform
    # GET requests at '/people/<lastname>'.
    'additional_lookup': {
        'url': 'regex("[\w]+")',
        'field': 'name'
    },

    # We choose to override global cache-control directives for this resource.
    'cache_control': 'max-age=10,must-revalidate',
    'cache_expires': 10,
    'allowed_roles': ['user','admin'],
    'allowed_item_roles': ['user', 'admin'],
    'public_methods': ['GET'],
    'public_item_methods': ['GET'],
    # most global settings can be overridden at resource level
    #'item_methods': ['GET', 'POST','PATCH','DELETE'],

    'schema': bridgeschema
}
accounts = {
    # the standard account entry point is defined as
    # '/accounts/<ObjectId>'. We define  an additional read-only entry
    # point accessible at '/accounts/<username>'.
    'additional_lookup': {
        'url': 'regex("[\w]+")',
        'field': 'username',
    },

    # We also disable endpoint caching as we don't want client apps to
    # cache account data.
    'cache_control': '',
    'cache_expires': 0,

    'allowed_roles': ['admin'],
    'allowed_item_roles': ['admin'],
    # Finally, let's add the schema definition for this endpoint.
    'schema': passschema,
}

SERVER_NAME = 'smartcampus.jimenez.lu:5000'
DOMAIN = {
    'bridges': bridges,
    'sensors': sensors,
    #'accounts': accounts
}
MONGO_HOST = 'localhost'
MONGO_PORT = 27017
MONGO_DBNAME = 'ConfigDatabase'
RESOURCE_METHODS = ['GET', 'POST', 'DELETE']
ITEM_METHODS = ['GET', 'PATCH', 'PUT', 'DELETE']


