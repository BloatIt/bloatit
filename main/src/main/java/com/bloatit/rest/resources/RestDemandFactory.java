package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.DemandFactory;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestDemandFactory extends RestElement{ 
    private DemandFactory model;

    protected RestDemandFactory(DemandFactory model){
        this.model=model;
    }

    @REST(name = "demandfactorys", method = RequestMethod.GET)
    public static RestDemandFactory getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "demandfactorys", method = RequestMethod.GET)
    public static RestList<RestDemandFactory> getAll(){
        //TODO auto generated code
        return null;
    }

    DemandFactory getModel(){
        return model;
    }
}
