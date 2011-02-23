package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.ExternalAccount;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
