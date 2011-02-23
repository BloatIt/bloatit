package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Transaction;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestTransactionList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestTransaction extends RestElement<Transaction>{ 
    private Transaction model;

    protected RestTransaction(Transaction model){
        this.model=model;
    }

    @REST(name = "transactions", method = RequestMethod.GET)
    public static RestTransaction getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "transactions", method = RequestMethod.GET)
    public static RestTransactionList getAll(){
        //TODO auto generated code
        return null;
    }

    Transaction getModel(){
        return model;
    }
}
