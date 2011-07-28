'''
Created on 27 juil. 2011

@author: fred
'''
from xml.dom.minidom import parseString as parse_xml
import urllib
import field

class Server:

    def __init__(self):
        self.host = 'http://elveos.org'
        self.base_rest = '/rest/'
        self.entity_cache = dict()
        self.access_token = None

    def set_host(self, host):
        self.host = host
    
    def load_entity(self,entity):
        manager = type(entity).objects
        url = self._build_entity_url(manager, entity.id);
        dom = parse_xml(urllib.urlopen(url).read())
        
        xml_rest_node = dom.getElementsByTagName('rest').item(0)
        
        
        xml_entity_node = xml_rest_node.getElementsByTagName(manager.code).item(0)
        
        
        
        for attribute in type(entity).__dict__:
            if attribute.startswith('__'):
                continue
            subclass = type(entity).__dict__[attribute]
            if issubclass(subclass.__class__ , field.Field):
                type(entity).__dict__[attribute].add_attribute(entity, xml_entity_node, attribute)
        
    def load_entity_collection(self,manager):
        url = self._build_entity_collection_url(manager);
        
        dom = parse_xml(urllib.urlopen(url).read())
        
        xml_rest = dom.getElementsByTagName('rest').item(0)
        
        xml_collection = xml_rest.getElementsByTagName(manager.collection_code).item(0)
        
        xml_entities = xml_collection.getElementsByTagName(manager.code)
        
        entity_list = list()
        
        exec 'from '+manager.base_module+' import '+manager.base_class
        
        entity_class = locals()[manager.base_class]
            
        for entity in xml_entities:
            id = int(entity.childNodes[0].data)
            obj = server.create_entity(entity_class, id)
            entity_list.append(obj)
        
        return entity_list
        
    
    def _build_entity_url(self, manager, id):
        return self.host + self.base_rest + manager.collection_code + '/' + str(id)+ (self.access_token and ('?access_token='+self.access_token) or '')
    
    def _build_entity_collection_url(self, manager):
        return self.host + self.base_rest + manager.collection_code
  
    
    def create_entity(self, entity_class, id):
        if id in self.entity_cache:
            return self.entity_cache[id]
        else:
            obj = entity_class()
            obj.id = id;
            self.entity_cache[id] = obj
            return obj
    
    def authenticate(self, access_token):
        self.access_token = access_token
        print 'Authenticate with %s' % access_token
    
server = Server()


def set_host(host):
        server.set_host(host)

def authenticate(access_token):
        server.authenticate(access_token)
