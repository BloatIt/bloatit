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
import com.bloatit.model.InternalAccount;
import com.bloatit.rest.list.RestInternalAccountList;
import com.bloatit.rest.list.RestTransactionList;

/**
 * <p>
 * Representation of a InternalAccount for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from InternalAccount that needs to be
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
 * return a RestInternalAccountList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestInternalAccount extends RestElement<InternalAccount> {
    private InternalAccount model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestInternalAccount() {
    }

    protected RestInternalAccount(InternalAccount model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestInternalAccount matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestInternalAccount
     */
    @REST(name = "internalaccounts", method = RequestMethod.GET)
    public static RestInternalAccount getById(int id) {
        // TODO auto generated code
        // RestInternalAccount restInternalAccount = new
        // RestInternalAccount(InternalAccountManager.getInternalAccountById(id));
        // if (restInternalAccount.isNull()) {
        // return null;
        // }
        // return restInternalAccount;
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestInternalAccount
     * </p>
     */
    @REST(name = "internalaccounts", method = RequestMethod.GET)
    public static RestInternalAccountList getAll() {
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
     * @see com.bloatit.model.InternalAccount#getBlocked()
     */
    // @XmlElement
    public BigDecimal getBlocked() throws RestException {
        // TODO auto-generated code stub
        try {
            BigDecimal blocked = model.getBlocked();
            return blocked;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getBlocked on InternalAccount", e);
        }
    }

    /**
     * @see com.bloatit.model.Account#getCreationDate()
     */
    // @XmlElement
    public Date getCreationDate() throws RestException {
        // TODO auto-generated code stub
        try {
            Date creationDate = model.getCreationDate();
            return creationDate;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getCreationDate on InternalAccount", e);
        }
    }

    /**
     * @see com.bloatit.model.Account#getAmount()
     */
    // @XmlElement
    public BigDecimal getAmount() throws RestException {
        // TODO auto-generated code stub
        try {
            BigDecimal amount = model.getAmount();
            return amount;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getAmount on InternalAccount", e);
        }
    }

    /**
     * @see com.bloatit.model.Account#getTransactions()
     */
    // @XmlElement
    public RestTransactionList getTransactions() throws RestException {
        // TODO auto-generated code stub
        try {
            return new RestTransactionList(model.getTransactions());
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getTransactions on InternalAccount", e);
        }
    }

    /**
     * @see com.bloatit.model.Account#getLastModificationDate()
     */
    // @XmlElement
    public Date getLastModificationDate() throws RestException {
        // TODO auto-generated code stub
        try {
            Date lastModificationDate = model.getLastModificationDate();
            return lastModificationDate;
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to use getLastModificationDate on InternalAccount", e);
        }
    }

    // XXX Do something
    // /**
    // * @see com.bloatit.model.Account#getActor()
    // */
    // // @XmlElement
    // public RestActor getActor() throws RestException {
    // // TODO auto-generated code stub
    // try {
    // RestActor actor = new RestActor(model.getActor());
    // return actor;
    // } catch (UnauthorizedOperationException e) {
    // throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED,
    // "Not allowed to use getActor on InternalAccount", e);
    // }
    // }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(InternalAccount model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    InternalAccount getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
