package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.BankTransaction;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestBankTransactionList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestBankTransaction extends RestElement<BankTransaction>{ 
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
    public static RestBankTransactionList getAll(){
        //TODO auto generated code
        return null;
    }

    BankTransaction getModel(){
        return model;
    }
}
