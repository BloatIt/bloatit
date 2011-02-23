package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.BankTransaction;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestBankTransaction extends RestElement{ 
    private BankTransaction model;

    protected RestBankTransaction(BankTransaction model){
        this.model=model;
    }

    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestBankTransaction getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestList<RestBankTransaction> getAll(){
        //TODO auto generated code
        return null;
    }

    BankTransaction getModel(){
        return model;
    }
}
