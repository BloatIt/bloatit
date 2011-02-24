package com.bloatit.rest.resources;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.data.DaoBankTransaction.State;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.BankTransaction;
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
    }

    protected RestBankTransaction(BankTransaction model) {
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
    public static RestBankTransaction getById(int id) {
        // TODO auto generated code
        // RestBankTransaction restBankTransaction = new
        // RestBankTransaction(BankTransactionManager.getBankTransactionById(id));
        // if (restBankTransaction.isNull()) {
        // return null;
        // }
        // return restBankTransaction;
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestBankTransaction
     * </p>
     */
    @REST(name = "banktransactions", method = RequestMethod.GET)
    public static RestBankTransactionList getAll() {
        // TODO auto generated code
        return null;
    }

    // ---------------------------------------------------------------------------------------
    // -- XML Getters
    // ---------------------------------------------------------------------------------------

    // TODO Generate

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

    /**
     * @see com.bloatit.model.BankTransaction#getValue()
     */
    // @XmlElement
    public BigDecimal getValue() {
        // TODO auto-generated code stub
        BigDecimal value = model.getValue();
        return value;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getMessage()
     */
    // @XmlElement
    public String getMessage() {
        // TODO auto-generated code stub
        String message = model.getMessage();
        return message;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getState()
     */
    // @XmlElement
    public State getState() {
        // TODO auto-generated code stub
        State state = model.getState();
        return state;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getCreationDate()
     */
    // @XmlElement
    public Date getCreationDate() {
        // TODO auto-generated code stub
        Date creationDate = model.getCreationDate();
        return creationDate;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getModificationDate()
     */
    // @XmlElement
    public Date getModificationDate() {
        // TODO auto-generated code stub
        Date modificationDate = model.getModificationDate();
        return modificationDate;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getReference()
     */
    // @XmlElement
    public String getReference() {
        // TODO auto-generated code stub
        String reference = model.getReference();
        return reference;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getToken()
     */
    // @XmlElement
    public String getToken() {
        // TODO auto-generated code stub
        String token = model.getToken();
        return token;
    }

    /**
     * @see com.bloatit.model.BankTransaction#getProcessInformations()
     */
    // @XmlElement
    public String getProcessInformations() {
        // TODO auto-generated code stub
        String processInformations = model.getProcessInformations();
        return processInformations;
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(BankTransaction model) {
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
