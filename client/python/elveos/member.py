from entity import Entity

class Member(Entity):
    
    
    def _get_name(self):
        return self._name;
        
    def _set_name(self):
        pass

    name = property(_get_name, _set_name)

        

