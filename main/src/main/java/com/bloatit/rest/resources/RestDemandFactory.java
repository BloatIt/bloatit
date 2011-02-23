package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.DemandFactory;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

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
