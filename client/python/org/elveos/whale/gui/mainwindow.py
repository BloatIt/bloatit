'''
Created on 28 juil. 2011

@author: fred
'''
from PyQt4 import QtGui
from PyQt4 import QtCore
from org.elveos.whale.gui.memberspanel import MembersPanel
from org.elveos.whale.gui.emptypanel import EmptyPanel

class MainWindow(QtGui.QWidget):
    

    def __init__(self):
        super(MainWindow, self).__init__()

        self.initUI()
        self.left_panel_child = None;
        self.center_panel_child = None;
        self.right_panel_child = None;
        
    def initUI(self):
        
        hbox = QtGui.QHBoxLayout(self)
        
        
        self.left_panel = QtGui.QWidget()
        self.center_panel = QtGui.QWidget()
        self.right_panel = QtGui.QWidget()
        
        self.left_panel.setLayout(QtGui.QHBoxLayout(self.left_panel))
        self.center_panel.setLayout(QtGui.QHBoxLayout(self.center_panel))
        self.right_panel.setLayout(QtGui.QHBoxLayout(self.right_panel))
        
        
        splitter = QtGui.QSplitter(QtCore.Qt.Horizontal)
        splitter.addWidget(self.left_panel)
        splitter.addWidget(self.center_panel)
        splitter.addWidget(self.right_panel)
        
        hbox.addWidget(splitter)
        
        
        self.setLayout(hbox)
        
        self.setWindowTitle('Elveos Whale')
        self.setWindowState(QtCore.Qt.WindowMaximized)
        self.resize(800, 600)
         
    def set_left_panel(self, panel):  
        
        if self.left_panel_child: 
            self.left_panel.layout().removeWidget(self.left_panel_child)
        
        self.left_panel_child = panel
        self.left_panel.layout().addWidget(panel)
     
    def set_center_panel(self, panel):  
        
        if self.center_panel_child: 
            self.center_panel.layout().removeWidget(self.center_panel_child)
        
        self.center_panel_child = panel
        self.center_panel.layout().addWidget(panel)
        
    def set_right_panel(self, panel):  
        
        if self.right_panel_child: 
            self.right_panel.layout().removeWidget(self.right_panel_child)
        
        self.right_panel_child = panel
        self.right_panel.layout().addWidget(panel)
    
    
        
    
