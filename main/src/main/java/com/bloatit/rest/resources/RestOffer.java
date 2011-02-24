package com.bloatit.rest.resources;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.bloatit.rest.list.RestOfferList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.model.Offer;

/**
* <p>
* Representation of a Offer for the ReST RPC calls
* </p>
* <p>
 * This class should implement any methods from Offer that needs to be called
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
* return a RestOfferList</li>
* </p>
*/
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestOffer extends RestElement<Offer>{ 
    private Offer model;


    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB 
     */
    @SuppressWarnings("unused")
    private RestOffer() {
    }

    protected RestOffer(Offer model){
        this.model=model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestOffer matching the <code>id</code> 
     * </p>
     * @param id the id of the RestOffer
     */
    @REST(name = "offers", method = RequestMethod.GET)
    public static RestOffer getById(int id){
        // TODO auto generated code
        // RestOffer restOffer = OfferManager.getById(id); 
        // if(restOffer == null) { 
        //     return null;
        // }
        return null;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestOffer 
     * </p>
     */
    @REST(name = "offers", method = RequestMethod.GET)
    public static RestOfferList getAll(){
        //TODO auto generated code
        return null;
    }

    // ---------------------------------------------------------------------------------------
    // -- XML Getters
    // ---------------------------------------------------------------------------------------

    // TODO Generate 

    @XmlAttribute
    @XmlID
    public String getId(){
        return model.getId().toString();
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB 
     */
     void setModel(Offer model){
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Offer getModel(){
        return model;
    }

    @Override
    public boolean isNull(){
        return (model == null);
    }

}
