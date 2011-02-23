package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Payline;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
