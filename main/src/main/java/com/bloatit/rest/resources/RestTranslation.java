package com.bloatit.rest.resources;

import java.util.Date;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Translation;
import com.bloatit.rest.list.RestFileMetadataList;
import com.bloatit.rest.list.RestTranslationList;

/**
 * <p>
 * Representation of a Translation for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Translation that needs to be
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
 * return a RestTranslationList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestTranslation extends RestElement<Translation> {
    private Translation model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestTranslation() {
    }

    protected RestTranslation(Translation model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestTranslation matching the <code>id</code>
     * </p>
     *
     * @param id the id of the RestTranslation
     */
    @REST(name = "translations", method = RequestMethod.GET)
    public static RestTranslation getById(int id) {
        // TODO auto generated code
        // RestTranslation restTranslation = new
        // RestTranslation(TranslationManager.getTranslationById(id));
        // if (restTranslation.isNull()) {
        // return null;
        // }
        // return restTranslation;
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestTranslation
     * </p>
     */
    @REST(name = "translations", method = RequestMethod.GET)
    public static RestTranslationList getAll() {
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
     * @see com.bloatit.model.Translation#getTitle()
     */
    // @XmlElement
    public String getTitle() {
        // TODO auto-generated code stub
        String title = model.getTitle();
        return title;
    }

    /**
     * @see com.bloatit.model.Translation#getText()
     */
    // @XmlElement
    public String getText() {
        // TODO auto-generated code stub
        String text = model.getText();
        return text;
    }

    /**
     * @see com.bloatit.model.Translation#getLocale()
     */
    // @XmlElement
    public Locale getLocale() {
        // TODO auto-generated code stub
        Locale locale = model.getLocale();
        return locale;
    }

    /**
     * @see com.bloatit.model.Kudosable#getState()
     */
    // @XmlElement
    public PopularityState getState() {
        // TODO auto-generated code stub
        PopularityState state = model.getState();
        return state;
    }

    /**
     * @see com.bloatit.model.Kudosable#getPopularity()
     */
    // @XmlElement
    public int getPopularity() {
        // TODO auto-generated code stub
        int popularity = model.getPopularity();
        return popularity;
    }

    /**
     * @see com.bloatit.model.Kudosable#getUserVoteValue()
     */
    // @XmlElement
    public int getUserVoteValue() {
        // TODO auto-generated code stub
        int userVoteValue = model.getUserVoteValue();
        return userVoteValue;
    }

    /**
     * @see com.bloatit.model.UserContent#getCreationDate()
     */
    // @XmlElement
    public Date getCreationDate() {
        // TODO auto-generated code stub
        Date creationDate = model.getCreationDate();
        return creationDate;
    }

    /**
     * @see com.bloatit.model.UserContent#getAuthor()
     */
    // @XmlElement
    public RestMember getAuthor() {
        // TODO auto-generated code stub
        RestMember author = new RestMember(model.getAuthor());
        return author;
    }

    /**
     * @see com.bloatit.model.UserContent#getAsGroup()
     */
    // @XmlElement
    public RestGroup getAsGroup() {
        // TODO auto-generated code stub
        RestGroup asGroup = new RestGroup(model.getAsGroup());
        return asGroup;
    }

    /**
     * @see com.bloatit.model.UserContent#getFiles()
     */
    // @XmlElement
    public RestFileMetadataList getFiles() {
        // TODO auto-generated code stub
        // RestList<?> files = new RestList<?>();
        return new RestFileMetadataList(model.getFiles());
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Translation model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Translation getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
