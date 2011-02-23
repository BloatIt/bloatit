package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Kudos;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
