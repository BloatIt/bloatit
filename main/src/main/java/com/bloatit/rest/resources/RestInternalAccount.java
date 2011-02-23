package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.InternalAccount;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestInternalAccount extends RestElement{ 
    private InternalAccount model;

    protected RestInternalAccount(InternalAccount model){
        this.model=model;
    }

    @REST(name = "internalaccounts", method = RequestMethod.GET)
    public static RestInternalAccount getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "internalaccounts", method = RequestMethod.GET)
    public static RestList<RestInternalAccount> getAll(){
        //TODO auto generated code
        return null;
    }

    InternalAccount getModel(){
        return model;
    }
}
