package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Bug;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestBug extends RestElement{ 
    private Bug model;

    protected RestBug(Bug model){
        this.model=model;
    }

    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBug getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestList<RestBug> getAll(){
        //TODO auto generated code
        return null;
    }

    Bug getModel(){
        return model;
    }
}
