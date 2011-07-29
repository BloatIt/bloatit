from entitymanager import EntityManager
import server

class MetaEntity(type):
    def __new__(cls, name, bases, dct):
              
        dct['objects'] = EntityManager(name, dct['__module__'] )
        
        return type.__new__(cls, name, bases, dct)

    
    
    
class Entity(object):
    objects = None
    __metaclass__ = MetaEntity
    
    
    def __init__(self):
        # Useless. Here just for Pydev
        self.objects = None
        self._loaded = False
        

    def __getattribute__(self, name):
        if name is 'id' or name.startswith('_'):
            return object.__getattribute__(self, name)
        
        if not self._loaded:
            self._load()
            self._loaded = True
        
        return object.__getattribute__(self, name)
    
    
    def _load(self):
        server.server.load_entity(self)
        
     


