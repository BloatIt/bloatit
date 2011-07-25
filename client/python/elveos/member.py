from entity import Entity

class Member(Entity):
    
    code="members"

    
    def __init__(self, id):
        self._name="Fred"
    
    def _get_name(self):
        return self._name;
        
    def _set_name(self):
        pass

    name = property(_get_name, _set_name)    

        

