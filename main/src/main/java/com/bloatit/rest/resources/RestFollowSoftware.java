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
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.data.queries.EmptyPageIterable;
import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.framework.restprocessor.RestServer.RequestMethod;
import com.bloatit.framework.restprocessor.annotations.REST;
import com.bloatit.framework.restprocessor.exception.RestException;
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.url.Loaders;
import com.bloatit.framework.xcgiserver.HttpReponseField.StatusCode;
import com.bloatit.model.FollowSoftware;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.managers.GenericManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.rest.list.RestFollowSoftwareList;

/**
 * <p>
 * Representation of a SoftwareFollow for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from BankTransaction that needs to be
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
 * return a RestBankTransactionList</li>
 * </p>
 */
@XmlRootElement(name = "followsoftware")
@XmlAccessorType(XmlAccessType.NONE)
public class RestFollowSoftware extends RestElement<FollowSoftware> {
    private FollowSoftware model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    @SuppressWarnings("unused")
    private RestFollowSoftware() {
        super();
    }

    protected RestFollowSoftware(final FollowSoftware model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * <p>
     * Finds the RestBankTransaction matching the <code>id</code>
     * </p>
     * 
     * @param id the id of the RestBankTransaction
     */
    @REST(name = "followsoftwares", method = RequestMethod.GET)
    public static RestFollowSoftware getById(final int id) {
        final RestFollowSoftware restFollowSoftware = new RestFollowSoftware((FollowSoftware) GenericManager.getById(id));
        if (restFollowSoftware.isNull()) {
            return null;
        }
        return restFollowSoftware;
    }

    /**
     * <p>
     * Finds the list of all (valid) RestFollowSoftware
     * </p>
     */
    @REST(name = "followsoftwares", method = RequestMethod.GET)
    public static RestFollowSoftwareList getAll() {
        if (AuthToken.isAuthenticated()) {
            return new RestFollowSoftwareList(AuthToken.getMember().getFollowedSoftware());
        }
        return new RestFollowSoftwareList(new EmptyPageIterable<FollowSoftware>());
    }

    @REST(name = "followsoftwares", method = RequestMethod.PUT, params = { "follower", "followed", "mail" })
    public static RestFollowSoftware createFollow(final String follower, final String followed, final String mail) throws RestException {

        try {
            final Member member = Loaders.fromStr(Member.class, follower);

            final Software software = Loaders.fromStr(Software.class, followed);
            final boolean isMail = Loaders.fromStr(Boolean.class, mail);

            final FollowSoftware followSoftware = member.followOrGetSoftware(software);
            followSoftware.setMail(isMail);

            final RestFollowSoftware restFollowSoftware = new RestFollowSoftware(followSoftware);
            if (restFollowSoftware.isNull()) {
                return null;
            }
            return restFollowSoftware;

        } catch (final ConversionErrorException e) {
            throw new RestException(StatusCode.ERROR_CLI_400_BAD_REQUEST, "Bad format for one of the parameters", e);
        } catch (final UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_CLI_403_FORBIDDEN, "Permission denied", e);
        }
    }

    /**
     * @param follower
     * @param followed
     * @return true is the follower was following the feature, false otherwise
     * @throws RestException
     */
    @REST(name = "followsoftwares", method = RequestMethod.DELETE, params = { "follower", "followed" })
    public static boolean deleteFollow(final String follower, final String followed) throws RestException {

        try {

            final Member member = Loaders.fromStr(Member.class, follower);

            final Software software = Loaders.fromStr(Software.class, followed);
            if (member.isFollowing(software)) {
                final FollowSoftware followSoftware = member.followOrGetSoftware(software);
                followSoftware.getFollower().unfollowSoftware(followSoftware.getFollowed());
                return true;
            } else {
                return false;
            }

        } catch (final ConversionErrorException e) {
            throw new RestException(StatusCode.ERROR_CLI_400_BAD_REQUEST, "Bad format for one of the parameters", e);
        } catch (final UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_CLI_403_FORBIDDEN, "Permission denied", e);
        }

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
     * @see com.bloatit.model.FollowSoftware#getFollowed()
     */
    @XmlElement
    @XmlIDREF
    public RestSoftware getFollowed() {
        return new RestSoftware(model.getFollowed());
    }

    /**
     * @see com.bloatit.model.FollowSoftware#getFollower()
     */
    @XmlElement
    public RestMember getFollower() {
        return new RestMember(model.getFollower());
    }

    @XmlAttribute
    public boolean isMail() {
        return model.isMail();
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    /**
     * Provided for JAXB
     */
    void setModel(final FollowSoftware model) {
        this.model = model;
    }

    /**
     * Package method to find the model
     */
    FollowSoftware getModel() {
        return model;
    }

    @Override
    public boolean isNull() {
        return (model == null);
    }

}
