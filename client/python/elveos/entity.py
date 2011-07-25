from entitymanager import EntityManager

class MetaEntity(type):
    def __new__(metacls, name, bases, dct):
        print metacls
        print name
        print bases
        print dct
        
        dct['objects'] = EntityManager(name, dct['__module__'] )
        
        return type.__new__(metacls, name, bases, dct)

    
class Entity(object):
    
    __metaclass__ = MetaEntity

    
    

