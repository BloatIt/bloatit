package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.BankTransaction;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestBankTransaction;

/**
 * <p>
 * Wraps a list of BankTransaction into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of BankTransaction<br />
 * Example:
 * 
 * <pre>
 * {@code <BankTransactions>}
 *     {@code <BankTransaction name=BankTransaction1 />}
 *     {@code <BankTransaction name=BankTransaction2 />}
 * {@code </BankTransactions>}
 * </pre>
 * <p>
 */
@XmlRootElement
public class RestBankTransactionList extends RestListBinder<RestBankTransaction, BankTransaction> {
    /**
     * Creates a RestBankTransactionList from a
     * {@codePageIterable<BankTransaction>}
     * 
     * @param collection the list of elements from the model
     */
    public RestBankTransactionList(final PageIterable<BankTransaction> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "banktransactions")
    @XmlElement(name = "banktransaction")
    public RestBankTransactionList getBankTransactions() {
        return this;
    }
}
