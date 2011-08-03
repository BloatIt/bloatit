'''
Created on 28 juil. 2011

@author: fred
'''

from PyQt4 import QtGui
from PyQt4 import QtCore
from PyQt4 import Qt
from org.elveos.model.member import Member
from org.elveos.whale.gui.entitypanel import EntityPanel

class MembersPanel(EntityPanel):
    

    def __init__(self):
        super(MembersPanel, self).__init__()

        self.initUI()
        
    def initUI(self):
        
        # Set layout
        grid = QtGui.QGridLayout()
        grid.setSpacing(10)
        self.setLayout(grid)
        
        
        # Title
        title = QtGui.QLabel('Members')
        grid.addWidget(title, 1, 0)


        member_list = Member.objects.all()

        # List
        members_list_view = QtGui.QListView()
        members_list_view.setModel(MembersListViewModel(member_list))
        grid.addWidget(members_list_view, 2, 0)
        
                       
                       
class MembersListViewModel(QtCore.QAbstractListModel):
    
    def __init__(self, member_list, parent=None):
        super (MembersListViewModel, self).__init__ (parent)
        self.member_list = member_list
    
    def rowCount(self, parent=QtCore.QModelIndex()):
        return len(self.member_list) 
 
    def data(self, index, role):
        if index.isValid() and role == QtCore.Qt.DisplayRole:
            return QtCore.QVariant(self.member_list[index.row()].name)
        else: 
            return QtCore.QVariant()    
        
        
    