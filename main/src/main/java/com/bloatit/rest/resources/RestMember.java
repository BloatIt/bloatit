package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.rest.RestElement;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.rest.list.RestMemberList;

/**
 * <p>
 * Representation of a Member for the ReST RPC calls
 * </p>
 * <p>
 * This class should implement any methods from Member that needs to be called
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
 * return a RestMemberList</li>
 * </p>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class RestMember extends RestElement<Member> {
    private Member model;

    // ---------------------------------------------------------------------------------------
    // -- Constructors
    // ---------------------------------------------------------------------------------------

    @SuppressWarnings("unused")
    private RestMember() {
    }

    protected RestMember(final Member model) {
        this.model = model;
    }

    // ---------------------------------------------------------------------------------------
    // -- Static methods
    // ---------------------------------------------------------------------------------------

    /**
     * Finds the RestMember matching the <code>id</code>
     * 
     * @param id the id of the RestMember
     * @throws RestException
     */
    @REST(name = "members", method = RequestMethod.GET)
    public static RestMember getById(final int id) {
        final RestMember restMember = new RestMember(MemberManager.getMemberById(id));
        if (restMember.isNull()) {
            return null;
        }
        return restMember;
    }

    /**
     * Finds the list of all (valid) RestMember
     */
    @REST(name = "members", method = RequestMethod.GET)
    public static RestMemberList getAll() {
        return new RestMemberList(MemberManager.getMembers());
    }

    // ---------------------------------------------------------------------------------------
    // -- XML Getters
    // ---------------------------------------------------------------------------------------

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

    // public PageIterable<JoinGroupInvitation> getReceivedInvitation(State
    // state) {
    // return model.getReceivedInvitation(state);
    // }

    @XmlAttribute(name = "name")
    public String getDisplayName() throws RestException {
        try {
            return model.getDisplayName();
        } catch (final UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to get name on user", e);
        }
    }

    @XmlElement(name = "karma")
    public int getKarma() throws RestException {
        try {
            return model.getKarma();
        } catch (final UnauthorizedOperationException e) {
            throw new RestException(StatusCode.ERROR_405_METHOD_NOT_ALLOWED, "Not allowed to get karma on user", e);
        }
    }

    // @XmlElement
    // public RestDemandList getDemands() {
    // return new RestDemandList(model.getDemands());
    // }

//    @XmlIDREF
    public RestFileMetadata getAvatar() {
        return new RestFileMetadata(model.getAvatar());
    }

    // ---------------------------------------------------------------------------------------
    // -- Utils
    // ---------------------------------------------------------------------------------------

    @Override
    public boolean isNull() {
        return (model == null);
    }

    /**
     * Package method to find the model
     */
    Member getModel() {
        return model;
    }

    void setModel(final Member model) {
        this.model = model;
    }

}
