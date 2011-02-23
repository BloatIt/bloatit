package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Contribution;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestContribution extends RestElement{ 
    private Contribution model;

    protected RestContribution(Contribution model){
        this.model=model;
    }

    @REST(name = "contributions", method = RequestMethod.GET)
    public static RestContribution getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "contributions", method = RequestMethod.GET)
    public static RestList<RestContribution> getAll(){
        //TODO auto generated code
        return null;
    }

    Contribution getModel(){
        return model;
    }
}
