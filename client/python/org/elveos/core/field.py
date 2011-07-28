'''
Created on 27 juil. 2011

@author: fred
'''

import entity
import server

class Field():
    pass     
        
class NodeField(Field):
    
    def __init__(self, type, path):
        self.type = type
        self.split_path = path.split('/')
        

    def add_attribute(self, entity_obj, xml_entity_node, field_name):
        
        current_node = xml_entity_node
        
        for sub_path in self.split_path:
            if sub_path:
                current_node = current_node.getElementsByTagName(sub_path).item(0)
        
        value = current_node.childNodes[0].data
        
        if issubclass(self.type, entity.Entity):
            obj = server.server.create_entity(self.type, int(value))
             
            entity_obj.__dict__[field_name] = obj
        else:
            entity_obj.__dict__[field_name] = self.type(value)
        
        
        

class AttributeField(Field):
    
    def __init__(self, type, path, name):
        self.type = type
        self.name = name
        self.split_path = path.split('/')

    def add_attribute(self, entity_obj, xml_entity_node, field_name):
        current_node = xml_entity_node
        
        for sub_path in self.split_path:
            if sub_path:
                current_node = current_node.getElementsByTagName(sub_path).item(0)
        
        value = current_node.getAttribute(self.name)
        
        if issubclass(self.type, entity.Entity):
            obj = server.server.create_entity(self.type, int(value))
             
            entity_obj.__dict__[field_name] = obj
        else:
            entity_obj.__dict__[field_name] = self.type(value)