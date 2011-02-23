package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.BankTransaction;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestBankTransactionList;

@XmlRootElement
public class RestBankTransaction extends RestElement<BankTransaction> {
    private final BankTransaction model;

    protected RestBankTransaction(final BankTransaction model) {
        this.model = model;
    }

    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestBankTransaction getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestBankTransactionList getAll() {
        // TODO auto generated code
        return null;
    }

    BankTransaction getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
