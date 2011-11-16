import elveos.core.field as field
from elveos.core.entity import Entity

class Member(Entity):
    name = field.AttributeField(str, '', 'name')
    email = field.AttributeField(str, '', 'email')
    karma = field.NodeField(int, 'karma')
       
    
        

