package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Payline;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestPayline extends RestElement{ 
    private Payline model;

    protected RestPayline(Payline model){
        this.model=model;
    }

    @REST(name = "paylines", method = RequestMethod.GET)
    public static RestPayline getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "paylines", method = RequestMethod.GET)
    public static RestList<RestPayline> getAll(){
        //TODO auto generated code
        return null;
    }

    Payline getModel(){
        return model;
    }
}
