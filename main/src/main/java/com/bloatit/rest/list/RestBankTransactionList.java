package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.BankTransaction;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestBankTransaction;

@XmlRootElement
public class RestBankTransactionList extends RestListBinder<RestBankTransaction, BankTransaction> {
    public RestBankTransactionList(PageIterable<BankTransaction> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "banktransactions")
    @XmlElement(name = "banktransaction")
    public RestBankTransactionList getBankTransactions() {
        return this;
    }
}

