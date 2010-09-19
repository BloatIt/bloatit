#!/usr/bin/python
from fcgi import WSGIServer
def app(environ, start_response):
	start_response('200 OK', [('Content-Type', 'text/html')])
	response = ""
	response += '''Hello world!\n'''
	response += "environ: "+str(environ)
	return(response)
WSGIServer(app).run()
