package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Offer;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestOffer extends RestElement{ 
    private Offer model;

    protected RestOffer(Offer model){
        this.model=model;
    }

    @REST(name = "offers", method = RequestMethod.GET)
    public static RestOffer getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "offers", method = RequestMethod.GET)
    public static RestList<RestOffer> getAll(){
        //TODO auto generated code
        return null;
    }

    Offer getModel(){
        return model;
    }
}
