/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

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
@XmlRootElement(name = "banktransactions")
public class RestBankTransactionList extends RestListBinder<RestBankTransaction, BankTransaction> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestBankTransactionList() {
        super();
    }

    /**
     * Creates a RestBankTransactionList from a
     * {@codePageIterable<BankTransaction>}
     *
     * @param collection the list of elements from the model
     */
    public RestBankTransactionList(PageIterable<BankTransaction> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "banktransaction")
    @XmlIDREF
    public List<RestBankTransaction> getBankTransactions() {
        List<RestBankTransaction> banktransactions = new ArrayList<RestBankTransaction>();
        for (RestBankTransaction banktransaction : this) {
            banktransactions.add(banktransaction);
        }
        return banktransactions;
    }
}
