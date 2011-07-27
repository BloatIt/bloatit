from xml.dom.minidom import parseString as parse_xml
import urllib

from server import server

class EntityManager():

    def __init__(self, base_class, base_module):
        self.base_class = base_class
        self.base_module = base_module
        self.code = base_class.lower()
        self.collection_code = self.code+'s'


    def __iter__(self):        
        
        entity_list = server.load_entity_collection(self)
        
        return ItEntity(entity_list)
        
        
        
        
            
class ItEntity:
    
    def __init__(self, entity_list):
        self.entity_list = entity_list
        self.gen = self.get_next()
        
    def get_next(self):
        for i in self.entity_list:
            yield i
        
        raise StopIteration    

        
    def next(self):
        return self.gen.next()        
        
    

