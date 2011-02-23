package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Kudos;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestKudos extends RestElement{ 
    private Kudos model;

    protected RestKudos(Kudos model){
        this.model=model;
    }

    @REST(name = "kudoss", method = RequestMethod.GET)
    public static RestKudos getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "kudoss", method = RequestMethod.GET)
    public static RestList<RestKudos> getAll(){
        //TODO auto generated code
        return null;
    }

    Kudos getModel(){
        return model;
    }
}
