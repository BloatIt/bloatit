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

import com.bloatit.data.DaoBatch.BatchState;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Batch;
import com.bloatit.model.managers.BatchManager;
import com.bloatit.rest.list.RestBatchList;
import com.bloatit.rest.list.RestReleaseList;

/**
 * <p>
 * Representation of a Batch for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Batch that needs to be called
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
 * return a RestBatchList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestBatch extends RestElement<Batch> {
    private Batch model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestBatch() {
    }

    protected RestBatch(Batch model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestBatch matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestBatch
     */
    @REST(name = "batchs", method = RequestMethod.GET)
    public static RestBatch getById(int id) {
        RestBatch restBatch = new RestBatch(BatchManager.getById(id));
        if (restBatch.isNull()) {
            return null;
        }
        return restBatch;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestBatch
     * </p>
     */
    @REST(name = "batchs", method = RequestMethod.GET)
    public static RestBatchList getAll() {
        return new RestBatchList(BatchManager.getAll());
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
     * @see com.bloatit.model.Batch#getOffer()
     */
    @XmlElement
    @XmlIDREF
    public RestOffer getOffer() {
        RestOffer offer = new RestOffer(model.getOffer());
        if (offer.isNull()) {
            return null;
        }
        return offer;
    }

    /**
     * @see com.bloatit.model.Batch#getReleaseDate()
     */
    @XmlAttribute
    public Date getReleaseDate() {
        return model.getReleaseDate();
    }

    /**
     * @see com.bloatit.model.Batch#getFatalBugsPercent()
     */
    @XmlElement
    public int getFatalBugsPercent() {
        return model.getFatalBugsPercent();
    }

    /**
     * @see com.bloatit.model.Batch#getMajorBugsPercent()
     */
    @XmlElement
    public int getMajorBugsPercent() {
        return model.getMajorBugsPercent();
    }

    /**
     * @see com.bloatit.model.Batch#getMinorBugsPercent()
     */
    @XmlElement
    public int getMinorBugsPercent() {
        return model.getMinorBugsPercent();
    }

    /**
     * @see com.bloatit.model.Batch#getExpirationDate()
     */
    @XmlAttribute
    public Date getExpirationDate() {
        return model.getExpirationDate();
    }

    /**
     * @see com.bloatit.model.Batch#getAmount()
     */
    @XmlAttribute
    public BigDecimal getAmount() {
        return model.getAmount();
    }

    /**
     * @see com.bloatit.model.Batch#getTitle()
     */
    @XmlElement
    public String getTitle() {
        return model.getTitle();
    }

    /**
     * @see com.bloatit.model.Batch#getDescription()
     */
    @XmlElement
    public String getDescription() {
        return model.getDescription();
    }

    /**
     * @see com.bloatit.model.Batch#getPosition()
     */
    @XmlElement
    public int getPosition() {
        return model.getPosition();
    }

    /**
     * @see com.bloatit.model.Batch#getBatchState()
     */
    @XmlElement
    public BatchState getBatchState() {
        return model.getBatchState();
    }

    /**
     * @see com.bloatit.model.Batch#getReleases()
     */
    @XmlElement
    public RestReleaseList getReleases() {
        return new RestReleaseList(model.getReleases());
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Batch model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Batch getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
