package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Transaction;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestTransactionList;

@XmlRootElement
public class RestTransaction extends RestElement<Transaction> {
    private final Transaction model;

    protected RestTransaction(final Transaction model) {
        this.model = model;
    }

    @REST(name = "transactions", method = RequestMethod.GET)
    public static RestTransaction getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "transactions", method = RequestMethod.GET)
    public static RestTransactionList getAll() {
        // TODO auto generated code
        return null;
    }

    Transaction getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
