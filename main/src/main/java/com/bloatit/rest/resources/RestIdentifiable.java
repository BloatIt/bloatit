package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Identifiable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestIdentifiable extends RestElement{ 
    private Identifiable model;

    protected RestIdentifiable(Identifiable model){
        this.model=model;
    }

    @REST(name = "identifiables", method = RequestMethod.GET)
    public static RestIdentifiable getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "identifiables", method = RequestMethod.GET)
    public static RestList<RestIdentifiable> getAll(){
        //TODO auto generated code
        return null;
    }

    Identifiable getModel(){
        return model;
    }
}
