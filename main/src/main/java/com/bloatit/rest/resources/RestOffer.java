package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Offer;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

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
