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

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.framework.restprocessor.RestServer.RequestMethod;
import com.bloatit.framework.restprocessor.annotations.REST;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.rest.adapters.DateAdapter;
import com.bloatit.rest.list.RestCommentList;
import com.bloatit.rest.list.RestContributionList;
import com.bloatit.rest.list.RestFeatureList;
import com.bloatit.rest.list.RestOfferList;

/**
 * <p>
 * Representation of a Feature for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Feature that needs to be called
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
 * return a RestFeatureList</li>
 * </p>
 */
@XmlRootElement(name = "feature")
@XmlAccessorType(XmlAccessType.NONE)
public class RestFeature extends RestElement<Feature> {
    private Feature model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestFeature() {
        super();
    }

    protected RestFeature(final Feature model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestFeature matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestFeature
     */
    @REST(name = "features", method = RequestMethod.GET)
    public static RestFeature getById(final int id) {
        final RestFeature restFeature = new RestFeature(FeatureManager.getFeatureById(id));
        if (restFeature.isNull()) {
            return null;
        }
        return restFeature;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestFeature
     * </p>
     */
    @REST(name = "features", method = RequestMethod.GET)
    public static RestFeatureList getAll() {
        return new RestFeatureList(FeatureManager.getFeatures());
    }

    // ---------------------------------------------------------------------------------------
    // -- XML Getters
    // ---------------------------------------------------------------------------------------

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

    @XmlAttribute
    @XmlIDREF
    public RestMember getAuthor() {
        return new RestMember(model.getMember());
    }

    @XmlAttribute
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreationDate() {
        return model.getCreationDate();
    }

    @XmlAttribute
    public PopularityState getState() {
        return model.getState();
    }

    @XmlElement
    public String getTitle() {
        return model.getTitle();
    }

    @XmlAttribute
    public int getPopularity() {
        return model.getPopularity();
    }

    @XmlElement
    public RestCommentList getComments() {
        return new RestCommentList(model.getComments());
    }

    @XmlElement
    public RestContributionList getContributions() {
        return new RestContributionList(model.getContributions());
    }

    @XmlAttribute
    public float getProgression() {
        return model.getProgression();
    }

    @XmlElement
    public BigDecimal getContribution() {
        return model.getContribution();
    }

    @XmlElement
    public RestDescription getDescription() {
        return new RestDescription(model.getDescription());
    }

    @XmlAttribute
    @XmlIDREF
    public RestSoftware getSoftware() {
        return new RestSoftware(model.getSoftware());
    }

    @XmlElement
    public RestOfferList getOffers() {
        return new RestOfferList(model.getOffers());
    }

    /**
     * Provided for JAXB
     */
    void setModel(final Feature model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    Feature getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
