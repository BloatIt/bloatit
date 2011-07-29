import field
from entity import Entity
from member import Member

class Feature(Entity):
    
    title = field.NodeField(str, 'description/defaultTranslation/title')
    description = field.NodeField(str, 'description/defaultTranslation/text')
    author = field.AttributeField(Member, '', 'author')
    
        
         
    
    


