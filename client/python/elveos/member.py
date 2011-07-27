import field
from entity import Entity

class Member(Entity):
    name = field.AttributeField(str, '', 'name')
    karma = field.NodeField(int, 'karma')
       
    
        

