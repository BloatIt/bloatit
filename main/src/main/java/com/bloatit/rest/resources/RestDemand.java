package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Demand;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestDemand extends RestElement{ 
    private Demand model;

    protected RestDemand(Demand model){
        this.model=model;
    }

    @REST(name = "demands", method = RequestMethod.GET)
    public static RestDemand getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "demands", method = RequestMethod.GET)
    public static RestList<RestDemand> getAll(){
        //TODO auto generated code
        return null;
    }

    Demand getModel(){
        return model;
    }
}
