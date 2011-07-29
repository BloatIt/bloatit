'''
Created on 28 juil. 2011

@author: fred
'''
from authenticator import Authenticator

import gtk  
import webkit  
import gobject  

from cgi import parse_qs

class GuiAuthenticator(Authenticator):
    
    
    
    def _get_auth_code(self):
        
        self._create_browser(self._get_auth_token_url())
        return self.auth_code
    
        

    def _extract_code(self, uri):
        split = uri.split('?', 1)
        query_string = ""
        if len(split) == 2:
            query_string = split[1]
        
        params = parse_qs(query_string)
        self.auth_code = params['code'][0]
    

    def _create_browser(self, url):

        gobject.threads_init()  
        self.window = gtk.Window()
        self.window.set_default_size(800, 600)
        self.window.connect("destroy", lambda a: gtk.main_quit()) 

        browser = webkit.WebView()  
        browser.connect("navigation-requested", self._navigation_requested)
        browser.open(url)
  
        self.window.add(browser)  
        self.window.show_all()  
        gtk.main()
        
    def _navigation_requested(self,webview, webframe, request):
        if request.get_uri().startswith('oauth://'):
            self.window.destroy() 
            self._extract_code(request.get_uri())
            return True
        return False

        