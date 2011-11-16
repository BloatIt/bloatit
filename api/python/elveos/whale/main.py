'''
Created on 28 juil. 2011

@author: fred
'''

import sys
from PyQt4 import QtGui

from elveos.core import server
from elveos.auth.guiauthenticator import GuiAuthenticator
from elveos.model.member import Member
from gui.mainwindow import MainWindow
import os
from elveos.whale.gui.memberspanel import MembersPanel
from elveos.whale.gui.emptypanel import EmptyPanel


class Whale():
    
    def __init__(self,args):

        if len(args) == 2:
            self.host = args[1]
        else:
            self.host = 'https://elveos.org'

    def run(self):
        
        
        
        self.load_config()
        # Configure elveos.org connection
        server.set_host(self.host)
        server.authenticate(self.access_token)

        #for member in Member.objects:
        #    print member.name + (member.email and " "+member.email or "")

        self.start_gui()
        

    def load_config(self):
        # Load config
        config_dir = os.path.expanduser("~/.local/share/elveos_whale")
        auth_file = "auth.db"
        
        
        
        
        if not os.path.exists(os.path.join(config_dir, auth_file)):
            self.access_token = GuiAuthenticator().authenticate()
            
            if not os.path.exists(config_dir):
                os.makedirs(config_dir)
            
            f = open(os.path.join(config_dir, auth_file), 'w')
            f.write(self.access_token)
            f.close()
        else:
            self.access_token = open(os.path.join(config_dir, auth_file)).read()

    def start_gui(self):
        
        app = QtGui.QApplication(sys.argv)
    
        main_window = MainWindow()
        main_window.show()
        
        main_window.load(Member.objects)
        
        sys.exit(app.exec_())
