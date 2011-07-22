from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import oauth2 as oauth

client = oauth.Client("python_client",
     "c'est un secret !",
     "http://127.0.0.1",
     redirect_uri="http://127.0.0.1:8080") 

class TestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        print "Path: %s" % self.path
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write('<h1>Welcome to the first web interface for elveos.org !</h1>')
        self.wfile.write('<a href="%s" >Authorize this site to access to your elveos datas</a>' % client.authorization_url(endpoint="fr/oauth_credential", params={"response_type": "code"}))

if __name__ == '__main__':
    server = HTTPServer(('', 8080), TestHandler)
    print('Test server started at http://127.0.0.1:8080')
    server.serve_forever()    
    
