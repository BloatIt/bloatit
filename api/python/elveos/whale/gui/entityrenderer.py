'''
Created on 28 juil. 2011

@author: fred
'''

from PyQt4 import QtGui
from PyQt4 import QtCore
from PyQt4 import Qt

class EntityRenderer(QtGui.QWidget):
    

    def __init__(self, model, parent):
        super(EntityRenderer, self).__init__()
        self.parent = parent
        self.model = model
        self.installEventFilter(self)
        
    def eventFilter(self, targer, event):

        if event.type() == QtCore.QEvent.MouseButtonRelease:
            self.open()
        return False
              
    def open(self):
        self.parent.open_entity(self.model)

    