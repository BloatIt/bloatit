'''
Created on 28 juil. 2011

@author: fred
'''
import oauth2 as oauth
import datetime

class Authenticator():
    
    
    
    def __init__(self):
        self.get_tokens_path='oauth/get_authtoken'
        self.host='http://127.0.0.1'
        self.redirect_uri= 'oauth://auth'
        self.client_id = 'be37629b2aa7186068de4a7d16d169579348dcf781a36865f0e0be73b31e880df077404e60b7724aa4295560c86f3d059771eda5e10038445210bb02b3ca4c78'
        self.client_secret = "c'est un secret !"   
        
    def authenticate(self):        
        self.oauth_client = oauth.Client(self.client_id, self.client_secret, self.host, redirect_uri=self.redirect_uri)
        
        auth_code = self._get_auth_code()
        
        
        response = self.oauth_client.access_token(auth_code, self.redirect_uri, endpoint=self.get_tokens_path, params={"grant_type": "authorization_code"})      

        self.access_token = response['access_token']
        self.refresh_token = response['refresh_token'] 
        self.expires = datetime.timedelta(seconds=int(response['expires_in']))
        
        return self.access_token
        
    def _get_auth_token_url(self):
        return self.oauth_client.authorization_url(endpoint="fr/oauth_credential", params={"response_type": "code"});

        
        
        
    
    