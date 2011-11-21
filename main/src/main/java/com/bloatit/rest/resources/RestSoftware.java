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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.framework.restprocessor.RestServer.RequestMethod;
import com.bloatit.framework.restprocessor.annotations.REST;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.rest.list.RestFeatureList;
import com.bloatit.rest.list.RestSoftwareList;

/**
 * <p>
 * Representation of a Software for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Software that needs to be called
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
 * return a RestSoftwareList</li>
 * </p>
 */
@XmlRootElement(name = "software")
@XmlAccessorType(XmlAccessType.NONE)
public class RestSoftware extends RestElement<Software> {
    private Software model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestSoftware() {
        super();
    }

    protected RestSoftware(final Software model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestSoftware matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestSoftware
     */
    @REST(name = "softwares", method = RequestMethod.GET)
    public static RestSoftware getById(final int id) {
        final RestSoftware restSoftware = new RestSoftware(SoftwareManager.getById(id));
        if (restSoftware.isNull()) {
            return null;
        }
        return restSoftware;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestSoftware
     * </p>
     */
    @REST(name = "softwares", method = RequestMethod.GET)
    public static RestSoftwareList getAll() {
        return new RestSoftwareList(SoftwareManager.getAll());
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
     * @see com.bloatit.model.Software#getName()
     */
    @XmlAttribute
    public String getName() {
        return model.getName();
    }

    /**
     * @see com.bloatit.model.Software#getDescription()
     */
    @XmlElement
    public RestDescription getDescription() {
        return new RestDescription(model.getDescription());
    }

    /**
     * @see com.bloatit.model.Software#getFeatures()
     */
    @XmlElement
    public RestFeatureList getFeatures() {
        return new RestFeatureList(model.getFeatures());
    }

    /**
     * @see com.bloatit.model.Software#getImage()
     */
    @XmlElement
    public RestFileMetadata getImage() {
        FileMetadata image = model.getImage();
        if (image != null) {
            return new RestFileMetadata(image);
        }
        return null;
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(final Software model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Software getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
