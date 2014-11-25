#ConfiG API

To install it, it requires:
python3
tornado (install with pip)
eve (install with pip)
a running instance of MONGODB

then modify run.py to add your own certificates
if you don't need ssl just remove the ssl options :
http_server = HTTPServer(WSGIContainer(app))

and settings.py (for the mongodb access and the nameserver(hostname))

To launch the application:
python3 run.py
