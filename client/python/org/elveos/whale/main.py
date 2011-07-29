'''
Created on 28 juil. 2011

@author: fred
'''

import sys
from PyQt4 import QtGui
from PyQt4 import QtCore

from org.elveos.core import server
from org.elveos.auth.guiauthenticator import GuiAuthenticator
from org.elveos.model.member import Member
from gui.mainwindow import MainWindow
import os


class Whale():

    def run(self):
        
        self.load_config()
        # Configure elveos.org connection
        server.set_host('https://elveos.org')
        server.authenticate(self.access_token)

        for member in Member.objects:
            print member.name + (member.email and " "+member.email or "")


        self.start_gui()
        

    def load_config(self):
        # Load config
        config_dir = os.path.expanduser("~/.local/share/elveos_whale")
        auth_file = "auth.db"
        
        if not os.path.exists(os.path.join(config_dir, auth_file)):
            self.access_token = GuiAuthenticator().authenticate()
            os.makedirs(config_dir)
            f = open(os.path.join(config_dir, auth_file), 'w')
            f.write(self.access_token)
            f.close()
        else:
            self.access_token = open(os.path.join(config_dir, auth_file)).read()

    def start_gui(self):
        
        app = QtGui.QApplication(sys.argv)
    
        widget = QtGui.QWidget()
        widget.setWindowState(QtCore.Qt.WindowMaximized)
        widget.setWindowTitle('Elveos Whale')
        widget.show()
        
        sys.exit(app.exec_())
