package com.bloatit.rest.resources;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.model.Transaction;
import com.bloatit.rest.list.RestTransactionList;

/**
 * <p>
 * Representation of a Transaction for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Transaction that needs to be
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
 * return a RestTransactionList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestTransaction extends RestElement<Transaction> {
    private Transaction model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestTransaction() {
    }

    protected RestTransaction(Transaction model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestTransaction matching the <code>id</code>
     * </p>
     *
     * @param id the id of the RestTransaction
     */
    @REST(name = "transactions", method = RequestMethod.GET)
    public static RestTransaction getById(int id) {
        // TODO auto generated code
        // RestTransaction restTransaction = new
        // RestTransaction(TransactionManager.getTransactionById(id));
        // if (restTransaction.isNull()) {
        // return null;
        // }
        // return restTransaction;
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestTransaction
     * </p>
     */
    @REST(name = "transactions", method = RequestMethod.GET)
    public static RestTransactionList getAll() {
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
     * @see com.bloatit.model.Transaction#getCreationDate()
     */
    // @XmlElement
    public Date getCreationDate() throws RestException {
        // TODO auto-generated code stub
        try {
            Date creationDate = model.getCreationDate();
            return creationDate;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getCreationDate on Transaction", e);
        }
    }

    /**
     * @see com.bloatit.model.Transaction#getAmount()
     */
    // @XmlElement
    public BigDecimal getAmount() throws RestException {
        // TODO auto-generated code stub
        try {
            BigDecimal amount = model.getAmount();
            return amount;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getAmount on Transaction", e);
        }
    }

    /**
     * @see com.bloatit.model.Transaction#getFrom()
     */
    // @XmlElement
    public RestInternalAccount getFrom() throws RestException {
        // TODO auto-generated code stub
        try {
            RestInternalAccount from = new RestInternalAccount(model.getFrom());
            return from;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getFrom on Transaction", e);
        }
    }

    // XXX : Do something
    // /**
    // * @see com.bloatit.model.Transaction#getTo()
    // */
    // // @XmlElement
    // public RestAccount getTo() throws RestException {
    // // TODO auto-generated code stub
    // try{
    // RestAccount to = new RestAccount(model.getTo());
    // return to;
    // }catch(UnauthorizedOperationException e){
    // throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED,
    // "Not allowed to use getTo on Transaction", e);
    // }
    // }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Transaction model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Transaction getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
