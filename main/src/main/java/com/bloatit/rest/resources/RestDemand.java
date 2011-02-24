package com.bloatit.rest.resources;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Demand;
import com.bloatit.model.Description;
import com.bloatit.model.Offer;
import com.bloatit.rest.list.RestDemandList;

/**
 * <p>
 * Representation of a Demand for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Demand that needs to be called
 * through the ReST RPC. Every such method needs to be mapped with the
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
 * return a RestDemandList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestDemand extends RestElement<Demand> {
    private Demand model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestDemand() {
    }

    protected RestDemand(Demand model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestDemand matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestDemand
     */
    @REST(name = "demands", method = RequestMethod.GET)
    public static RestDemand getById(int id) {
        // TODO auto generated code
        // RestDemand restDemand = DemandManager.getById(id);
        // if(restDemand == null) {
        // return null;
        // }
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestDemand
     * </p>
     */
    @REST(name = "demands", method = RequestMethod.GET)
    public static RestDemandList getAll() {
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

    @XmlAttribute
    @XmlIDREF
    public RestMember getAuthor() {
        return new RestMember(model.getAuthor());
    }

    @XmlAttribute
    public Date getCreationDate() {
        return model.getCreationDate();
    }

//    @XmlAttribute
    public PopularityState getState() {
        return model.getState();
    }

    @XmlAttribute
    public int getPopularity() {
        return model.getPopularity();
    }

    // @XmlElement
    public PageIterable<Comment> getComments() throws RestException {
        try {
            return model.getComments();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getComments()");
        }
    }

    // @XmlElement
    public PageIterable<Contribution> getContributions() throws RestException {

        try {
            return model.getContributions();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getContributions()");
        }
    }

    @XmlAttribute
    public float getProgression() throws RestException {
        try {
            return model.getProgression();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getProgression()");
        }
    }

    @XmlElement
    public BigDecimal getContribution() throws RestException {
        try {
            return model.getContribution();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getContribution()");
        }
    }

//    @XmlElement
    public Description getDescription() throws RestException {
        try {
            return model.getDescription();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getDescription()");
        }
    }

    @XmlAttribute
    @XmlIDREF
    public RestProject getProject() throws RestException {
        try {
            return new RestProject(model.getProject());
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getProject()");
        }
    }

//    @XmlElement
    public PageIterable<Offer> getOffers() throws RestException {
        try {
            return model.getOffers();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getOffers()");
        }
    }

    @XmlElement
    public String getTitle() throws RestException {
        try {
            return model.getTitle();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getTitle()");
        }
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Demand model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Demand getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
