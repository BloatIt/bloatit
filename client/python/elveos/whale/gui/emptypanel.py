'''
Created on 28 juil. 2011

@author: fred
'''

from PyQt4 import QtGui
from PyQt4 import QtCore
from PyQt4 import Qt
from org.elveos.model.member import Member
from org.elveos.whale.gui.entitypanel import EntityPanel

class EmptyPanel(EntityPanel):
    

    def __init__(self):
        super(EmptyPanel, self).__init__()

        self.initUI()
        
    def initUI(self):
        
        # Set layout
        grid = QtGui.QGridLayout()
        grid.setSpacing(10)
        self.setLayout(grid)
        
        # Title
        title = QtGui.QLabel('Empty')
        grid.addWidget(title, 1, 0)


        
            
        
    