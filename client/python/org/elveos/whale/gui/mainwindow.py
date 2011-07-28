'''
Created on 28 juil. 2011

@author: fred
'''
from PyQt4 import QtGui



class MainWindow(QtGui.QWidget):
    

    def __init__(self):
        super(MainWindow, self).__init__()

        self.initUI()
        
        
    def initUI(self):
        title = QtGui.QLabel('Title')
        author = QtGui.QLabel('Author')
        review = QtGui.QLabel('Review')

        titleEdit = QtGui.QLineEdit()
        authorEdit = QtGui.QLineEdit()
        reviewEdit = QtGui.QTextEdit()

        grid = QtGui.QGridLayout()
        grid.setSpacing(10)

        grid.addWidget(title, 1, 0)
        grid.addWidget(titleEdit, 1, 1)

        grid.addWidget(author, 2, 0)
        grid.addWidget(authorEdit, 2, 1)

        grid.addWidget(review, 3, 0)
        grid.addWidget(reviewEdit, 3, 1, 5, 1)
        
        self.setLayout(grid)
        
        self.setWindowTitle('grid layout')
        self.resize(350, 300)
         
    