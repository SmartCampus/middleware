__author__ = 'matthieujimenez'
from eve import Eve
from eve.auth import BasicAuth
from werkzeug.security import check_password_hash
from tornado.wsgi import WSGIContainer
from tornado.httpserver import HTTPServer
from tornado.ioloop import IOLoop

class RolesAuth(BasicAuth):
    def check_auth(self, username, password, allowed_roles, resource, method):
        # use Eve's own db driver; no additional connections/resources are used
        accounts = app.data.driver.db['accounts']
        lookup = {'username': username}
        if allowed_roles:
            # only retrieve a user if his roles match ``allowed_roles``
            lookup['roles'] = {'$in': allowed_roles}
        account = accounts.find_one(lookup)
        return account and check_password_hash(account['password'], password)

app = Eve()

if __name__ == '__main__':
    http_server = HTTPServer(WSGIContainer(app),ssl_options={
    "certfile":  "server.crt",
    "keyfile": "server.key",
    })
    http_server.listen(5000)
    IOLoop.instance().start()
