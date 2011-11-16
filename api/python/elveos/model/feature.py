import elveos.core.field as field
from elveos.core.entity import Entity
from member import Member

class Feature(Entity):
    
    title = field.NodeField(str, 'description/defaultTranslation/title')
    description = field.NodeField(str, 'description/defaultTranslation/text')
    author = field.AttributeField(Member, '', 'author')
    
        
         
    
    


