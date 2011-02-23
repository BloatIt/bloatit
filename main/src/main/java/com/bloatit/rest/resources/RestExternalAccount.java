package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.ExternalAccount;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestExternalAccount extends RestElement{ 
    private ExternalAccount model;

    protected RestExternalAccount(ExternalAccount model){
        this.model=model;
    }

    @REST(name = "externalaccounts", method = RequestMethod.GET)
    public static RestExternalAccount getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "externalaccounts", method = RequestMethod.GET)
    public static RestList<RestExternalAccount> getAll(){
        //TODO auto generated code
        return null;
    }

    ExternalAccount getModel(){
        return model;
    }
}
