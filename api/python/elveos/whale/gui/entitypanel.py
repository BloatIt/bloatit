'''
Created on 28 juil. 2011

@author: fred
'''

from PyQt4 import QtGui
from PyQt4 import QtCore
from PyQt4 import Qt

class EntityPanel(QtGui.QWidget):
    

    def __init__(self, parent):
        super(EntityPanel, self).__init__()
        self.parent = parent
        
              
    def open_entity(self, model):
        self.parent.open_entity(model)  

    