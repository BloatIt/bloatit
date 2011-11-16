from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
from cgi import parse_qs
import oauth2 as oauth
import urllib
import datetime
import cgi

OAUTH_REDIRECT_URI_BASE= '/oauth_auth'
OAUTH_REDIRECT_URI="http://127.0.0.1:8080" + OAUTH_REDIRECT_URI_BASE
OAUTH_GET_TOKEN_PATH='oauth/get_authtoken'
OAUTH_SERVER='http://127.0.0.1'


 

class TestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        print "Path: %s" % self.path

        split = self.path.split('?', 1)
        self.path = split[0]
        self.query_string = ""
        if len(split) == 2:
            self.query_string = split[1]
        
        if self.path.startswith(OAUTH_REDIRECT_URI_BASE):
            self.oauth_callback()
            return
        
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write('<h1>Welcome to the first web interface for elveos.org !</h1>')
        self.wfile.write('<a href="%s" >Authorize this site to access to your elveos datas</a>' % client.authorization_url(endpoint="fr/oauth_credential", params={"response_type": "code"}))

    def oauth_callback(self):
        params = parse_qs(self.query_string)
        code = params['code'][0]
        print code
        response = client.access_token(code, OAUTH_REDIRECT_URI, endpoint=OAUTH_GET_TOKEN_PATH, params={"grant_type": "authorization_code"})      
        
        access_token = response['access_token']
        refresh_token = response['refresh_token']
        expires = datetime.timedelta(seconds=int(response['expires_in']))

        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        
        
        self.wfile.write('<h1>Welcome to the first web interface for elveos.org !</h1>')
        self.wfile.write('<p>You succesfully autorize this website. I have your tokens: </p>')
        self.wfile.write('<ul>')
        self.wfile.write('<li>Access token: %s</li>' % access_token)
        self.wfile.write('<li>Refresh token: %s</li>' % refresh_token)
        self.wfile.write('<li>Expires in : %s</li>' % expires)
        self.wfile.write('<ul>')
        
        #389fe6cb0b7fbc6bfcdd8641283bbb
        
        self.wfile.write(cgi.escape(urllib.urlopen('http://127.0.0.1/rest/externalaccounts/99?access_token=%s' % access_token).read()))
        
        
        #grant_type=authorization_code
        #code=



if __name__ == '__main__':
    server = HTTPServer(('', 8080), TestHandler)
    print('Test server started at http://127.0.0.1:8080')
    server.serve_forever()    
    
