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

class MembersPanel(EntityPanel):
    

    def __init__(self, parent):
        super(MembersPanel, self).__init__(parent)

        self.initUI()
        
    def initUI(self):
        # Set layout
        layout = QtGui.QVBoxLayout()
        layout.setSpacing(0)
        self.setLayout(layout)
        
        
        # Title
        title = QtGui.QLabel('Members')
        layout.addWidget(title)

        count = len(Member.objects)
        
        scrollArea = QtGui.QScrollArea()
        
        scrollPane = QtGui.QWidget()
        layout2 = QtGui.QVBoxLayout()
        layout2.setSpacing(0)
        scrollPane.setLayout(layout2)
        # List
        for i, member in enumerate(Member.objects):
            layout2.addWidget(MemberRenderer(member, self))
            print ""+str(i+1)+"/"+str(count)
    
        scrollArea.setWidget(scrollPane)
        layout.addWidget(scrollArea)
    
        layout.addStretch()
        
                       
class MemberRenderer(EntityRenderer):
    
    def __init__(self, member, parent):
        super (MemberRenderer, self).__init__ (member, parent)
        self.initUI()
        
    def initUI(self):  
        # Set layout
        grid = QtGui.QGridLayout()
        grid.setSpacing(0)
        self.setLayout(grid)
        
        # Name
        title = QtGui.QLabel(self.model.name)
        grid.addWidget(title, 1, 0)
        
        
    
