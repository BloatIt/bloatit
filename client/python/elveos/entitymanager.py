from xml.dom.minidom import parseString as parse_xml
import urllib

class EntityManager():

    def __init__(self, base_class, base_module):
        self.base_class = base_class
        self.base_module = base_module
        self.code = base_class.lower()
        self.collection_code = self.code+'s'


    def __iter__(self):        
        dom = parse_xml(urllib.urlopen('http://127.0.0.1/rest/'+self.collection_code).read())
        
        xml_rest = dom.getElementsByTagName('rest').item(0)
        
        xml_collection = xml_rest.getElementsByTagName(self.collection_code).item(0)
        
        xml_entities = xml_collection.getElementsByTagName(self.code)
        
        self.entity_list = list()
        
        exec 'from '+self.base_module+' import '+self.base_class

        entity_class = locals()[self.base_class]
            
        for entity in xml_entities:
            id = int(entity.childNodes[0].data)
            obj = entity_class()
            obj.id = id;
            
            self.entity_list.append(obj)
        
        
        return ItEntity(self.entity_list)
        
        
        
        
            
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
        
    

