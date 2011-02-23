package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Transaction;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestTransaction;

@XmlRootElement
public class RestTransactionList extends RestListBinder<RestTransaction, Transaction> {
    public RestTransactionList(PageIterable<Transaction> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "transactions")
    @XmlElement(name = "transaction")
    public RestTransactionList getTransactions() {
        return this;
    }
}

