package com.bloatit.rest.resources;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Kudos;
import com.bloatit.model.managers.KudosManager;
import com.bloatit.rest.list.RestKudosList;

/**
 * <p>
 * Representation of a Kudos for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Kudos that needs to be called
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
 * return a RestKudosList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestKudos extends RestElement<Kudos> {
    private Kudos model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestKudos() {
    }

    protected RestKudos(Kudos model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestKudos matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestKudos
     */
    @REST(name = "kudos", method = RequestMethod.GET)
    public static RestKudos getById(int id) {
        RestKudos restKudos = new RestKudos(KudosManager.getById(id));
        if (restKudos.isNull()) {
            return null;
        }
        return restKudos;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestKudos
     * </p>
     */
    @REST(name = "kudoss", method = RequestMethod.GET)
    public static RestKudosList getAll() {
        return new RestKudosList(KudosManager.getAll());
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
     * @see com.bloatit.model.Kudos#getValue()
     */
    @XmlAttribute
    public int getValue() {
        return model.getValue();
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
        return new RestMember(model.getAuthor());
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(Kudos model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Kudos getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
