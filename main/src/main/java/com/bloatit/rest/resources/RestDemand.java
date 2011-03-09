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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.model.Demand;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.rest.adapters.DateAdapter;
import com.bloatit.rest.list.RestCommentList;
import com.bloatit.rest.list.RestContributionList;
import com.bloatit.rest.list.RestDemandList;
import com.bloatit.rest.list.RestOfferList;

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
@XmlRootElement(name = "demand")
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
        RestDemand restDemand = new RestDemand(DemandManager.getDemandById(id));
        if (restDemand.isNull()) {
            return null;
        }
        return restDemand;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestDemand
     * </p>
     */
    @REST(name = "demands", method = RequestMethod.GET)
    public static RestDemandList getAll() {
        return new RestDemandList(DemandManager.getDemands());
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
        return new RestMember(model.getAuthor());
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
    public String getTitle() throws RestException {
        try {
            return model.getTitle();
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getTitle()");
        }
    }

    @XmlAttribute
    public int getPopularity() {
        return model.getPopularity();
    }

    @XmlElement
    public RestCommentList getComments() throws RestException {
        try {
            return new RestCommentList(model.getComments());
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getComments()");
        }
    }

    @XmlElement
    public RestContributionList getContributions() throws RestException {
        try {
            return new RestContributionList(model.getContributions());
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

    @XmlElement
    public RestDescription getDescription() throws RestException {
        try {
            return new RestDescription(model.getDescription());
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

    @XmlElement
    public RestOfferList getOffers() throws RestException {
        try {
            return new RestOfferList(model.getOffers());
        } catch (UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_403_FORBIDDEN, "Not allowed to use Demand.getOffers()");
        }
    }

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
