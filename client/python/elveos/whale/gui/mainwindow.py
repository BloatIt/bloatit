'''
Created on 28 juil. 2011

@author: fred
'''
from PyQt4 import QtGui
from PyQt4 import QtCore
from elveos.whale.gui.memberspanel import MembersPanel
from elveos.whale.gui.memberpanel import MemberPanel
from elveos.whale.gui.emptypanel import EmptyPanel
from elveos.whale.gui.entitypanel import EntityPanel
from elveos.core.entitymanager import EntityManager
from elveos.model.member import Member

class MainWindow(QtGui.QWidget):
    

    def __init__(self):
        super(MainWindow, self).__init__()

        self.initUI()
        
    def initUI(self):
        
        hbox = QtGui.QHBoxLayout(self)
        
        
        self.left_panel = TopEntityPanel(self)
        self.center_panel = TopEntityPanel(self)
        self.right_panel = TopEntityPanel(self)
        
        self.left_panel.set_next_panel(self.center_panel)
        self.center_panel.set_next_panel(self.right_panel)
        self.right_panel.set_next_panel(self.left_panel)
        
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
        #self.setWindowState(QtCore.Qt.WindowMaximized)
        self.resize(800, 600)
    
    def load(self, loadable, target_panel=None):
        if target_panel:
            target_panel.load(loadable)
        else:
            self.left_panel.load(loadable)
    
class TopEntityPanel(EntityPanel):
        
    def __init__(self, window):
        super(TopEntityPanel, self).__init__(None)
        self.window = window
        self.current_child = None
        
        self.setLayout(QtGui.QHBoxLayout(self))
        
    def set_next_panel(self, next_panel):
        self.next_panel = next_panel
        
    def open_entity(self, model):
        self.window.load(model, self.next_panel)
        
        
    def load(self, loadable):
        if isinstance(loadable, EntityManager):
            if loadable.code == "member":
                self.load_panel(MembersPanel(self))
                return
        elif isinstance(loadable, Member):
            self.load_panel(MemberPanel(self, loadable))
            return

    def load_panel(self, panel):
        print "load_panel "+str(panel)
        
        if self.current_child:
            self.layout().removeWidget(self.current_child)
            self.current_child.hide()
        
        self.layout().addWidget(panel)
        self.current_child = panel
        
        
        