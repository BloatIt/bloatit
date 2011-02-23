package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Account;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestAccount extends RestElement{ 
    private Account model;

    protected RestAccount(Account model){
        this.model=model;
    }

    @REST(name = "accounts", method = RequestMethod.GET)
    public static RestAccount getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "accounts", method = RequestMethod.GET)
    public static RestList<RestAccount> getAll(){
        //TODO auto generated code
        return null;
    }

    Account getModel(){
        return model;
    }
}
