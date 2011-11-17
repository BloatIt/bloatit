'''
Created on 28 juil. 2011

@author: fred
'''

from PyQt4 import QtGui
from PyQt4 import QtCore
from PyQt4 import Qt
from elveos.model.member import Member
from elveos.whale.gui.entitypanel import EntityPanel
from elveos.whale.gui.entityrenderer import EntityRenderer

class MemberPanel(EntityPanel):
    

    def __init__(self, parent, member):
        super(MemberPanel, self).__init__(parent)
        self.member = member
        self.initUI()
        
    def initUI(self):
        
        # Set layout
        layout = QtGui.QVBoxLayout()
        layout.setSpacing(10)
        self.setLayout(layout)
        
        
        # Title
        title = QtGui.QLabel('Name: '+self.member.name)
        layout.addWidget(title)
        
        # Karma
        karma = QtGui.QLabel('Karma: '+str(self.member.karma))
        layout.addWidget(karma)
        
        # Email
        if self.member.email:
            email = QtGui.QLabel('Email: '+self.member.email)
            layout.addWidget(email)

        
        layout.addStretch()
