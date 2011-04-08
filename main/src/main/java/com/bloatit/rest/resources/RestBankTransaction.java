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
package com.bloatit.rest.resources;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.bloatit.data.DaoBankTransaction.State;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.managers.BankTransactionManager;
import com.bloatit.rest.adapters.DateAdapter;
import com.bloatit.rest.list.RestBankTransactionList;

/**
 * <p>
 * Representation of a BankTransaction for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from BankTransaction that needs to be
 * called through the ReST RPC. Every such method needs to be mapped with the
 * {@code @REST} interface.
 * <p>
 * ReST uses the four HTTP request methods <code>GET</code>, <code>POST</code>,
 * <code>PUT</code>, <code>DELETE</code> each with their own meaning. Please
 * only bind the according to the following:
 * <li>GET list: List the URIs and perhaps other details of the collection's
 * members.</li>
 * <li>GET list/id: Retrieve a representation of the addressed member of the
 * collection, expressed in an appropriate Internet media type.</li>
 * <li>POST list: Create a new entry in the collection. The new entry's URL is
 * assigned automatically and is usually returned by the operation.</li>
 * <li>POST list/id: Treat the addressed member as a collection in its own right
 * and create a new entry in it.</li>
 * <li>PUT list: Replace the entire collection with another collection.</li>
 * <li>PUT list/id: Replace the addressed member of the collection, or if it
 * doesn't exist, create it.</li>
 * <li>DELETE list: Delete the entire collection.</li>
 * <li>DELETE list/id: Delete the addressed member of the collection.</li>
 * </p>
 * </p>
 * <p>
 * This class will be serialized as XML (or maybe JSON who knows) to be sent
 * over to the client RPC. Hence this class needs to be annotated to indicate
 * which methods (and/or fields) are to be matched in the XML data. For this
 * use:
 * <li>@XmlRootElement at the root of the class</li>
 * <li>@XmlElement on each method/attribute that will yield <i>complex</i> data</li>
 * <li>@XmlAttribute on each method/attribute that will yield <i>simple</i> data
 * </li>
 * <li>Methods that return a list need to be annotated with @XmlElement and to
 * return a RestBankTransactionList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestBankTransaction extends RestElement<BankTransaction> {
    private BankTransaction model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestBankTransaction() {
        super();
    }

    protected RestBankTransaction(final BankTransaction model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestBankTransaction matching the <code>id</code>
     * </p>
     *
     * @param id the id of the RestBankTransaction
     */
    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestBankTransaction getById(final int id) {
        final RestBankTransaction restBankTransaction = new RestBankTransaction(BankTransactionManager.getById(id));
        if (restBankTransaction.isNull()) {
            return null;
        }
        return restBankTransaction;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestBankTransaction
     * </p>
     */
    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestBankTransactionList getAll() {
        return new RestBankTransactionList(BankTransactionManager.getAll());
    }

    // ---------------------------------------------------------------------------------------
    // -- XML Getters
    // ---------------------------------------------------------------------------------------

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getValuePaid()
     */
    @XmlAttribute
    public BigDecimal getValuePaid() {
        return model.getValuePaid();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getValue()
     */
    @XmlAttribute
    public BigDecimal getValue() {
        return model.getValue();
    }


    /**
     * @see com.bloatit.model.BankTransaction#getMessage()
     */
    @XmlElement
    public String getMessage() {
        return model.getMessage();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getState()
     */
    @XmlAttribute(name = "transactionstate")
    public State getTransactionState() {
        return model.getState();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getCreationDate()
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreationDate() {
        return model.getCreationDate();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getModificationDate()
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getModificationDate() {
        return model.getModificationDate();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getReference()
     */
    @XmlElement
    public String getReference() {
        return model.getReference();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getToken()
     */
    @XmlElement
    public String getToken() {
        return model.getToken();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getProcessInformations()
     */
    @XmlElement
    public String getProcessInformations() {
        return model.getProcessInformations();
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(final BankTransaction model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    BankTransaction getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
