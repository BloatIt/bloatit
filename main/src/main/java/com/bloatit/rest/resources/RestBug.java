package com.bloatit.rest.resources;

import java.util.Date;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoBug.State;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Bug;
import com.bloatit.model.managers.BugManager;
import com.bloatit.rest.list.RestBugList;
import com.bloatit.rest.list.RestCommentList;
import com.bloatit.rest.list.RestFileMetadataList;

/**
 * <p>
 * Representation of a Bug for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Bug that needs to be called
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
 * return a RestBugList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestBug extends RestElement<Bug> {
    private Bug model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestBug() {
    }

    protected RestBug(Bug model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestBug matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestBug
     */
    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBug getById(int id) {
        RestBug restBug = new RestBug(BugManager.getById(id));
        if (restBug.isNull()) {
            return null;
        }
        return restBug;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestBug
     * </p>
     */
    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBugList getAll() {
        return new RestBugList(BugManager.getAll());
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
     * @see com.bloatit.model.Bug#getState()
     */
    @XmlAttribute
    public State getState() {
        return model.getState();
    }

    /**
     * @see com.bloatit.model.Bug#getTitle()
     */
    @XmlElement
    public String getTitle() {
        return model.getTitle();
    }

    /**
     * @see com.bloatit.model.Bug#getDescription()
     */
    @XmlElement
    public String getDescription() {
        return model.getDescription();
    }

    /**
     * @see com.bloatit.model.Bug#getLocale()
     */
    @XmlAttribute
    public Locale getLocale() {
        return model.getLocale();
    }

    /**
     * @see com.bloatit.model.Bug#getComments()
     */
    @XmlElement
    public RestCommentList getComments() {
        return new RestCommentList(model.getComments());
    }

    /**
     * @see com.bloatit.model.Bug#getAssignedTo()
     */
    @XmlElement
    @XmlIDREF
    public RestMember getAssignedTo() {
        RestMember assignedTo = new RestMember(model.getAssignedTo());
        if (assignedTo.isNull()) {
            return null;
        }
        return assignedTo;
    }

    /**
     * @see com.bloatit.model.Bug#getErrorLevel()
     */
    @XmlAttribute
    public Level getErrorLevel() {
        return model.getErrorLevel();
    }

    /**
     * @see com.bloatit.model.Bug#getBatch()
     */
    @XmlElement
    public RestBatch getBatch() {
        RestBatch batch = new RestBatch(model.getBatch());
        if (batch.isNull()) {
            return null;
        }
        return batch;
    }

    /**
     * @see com.bloatit.model.Bug#getLastUpdateDate()
     */
    @XmlAttribute
    public Date getLastUpdateDate() {
        return model.getLastUpdateDate();
    }

    /**
     * @see com.bloatit.model.UserContent#getCreationDate()
     */
    @XmlAttribute
    public Date getCreationDate() {
        return model.getCreationDate();
    }

    /**
     * @see com.bloatit.model.UserContent#getAuthor()
     */
    @XmlAttribute
    @XmlIDREF
    public RestMember getAuthor() {
        RestMember author = new RestMember(model.getAuthor());
        if (author.isNull()) {
            return null;
        }
        return author;
    }

    /**
     * @see com.bloatit.model.UserContent#getAsGroup()
     */
    @XmlElement
    @XmlIDREF
    public RestGroup getAsGroup() {
        RestGroup asGroup = new RestGroup(model.getAsGroup());
        if (asGroup.isNull()) {
            return null;
        }
        return asGroup;
    }

    /**
     * @see com.bloatit.model.UserContent#getFiles()
     */
    @XmlElement
    public RestFileMetadataList getFiles() {
        return new RestFileMetadataList(model.getFiles());
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Bug model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Bug getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
