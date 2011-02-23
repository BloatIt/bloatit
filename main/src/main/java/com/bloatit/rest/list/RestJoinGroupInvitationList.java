package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestJoinGroupInvitation;

/**
 * <p>
 * Wraps a list of JoinGroupInvitation into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of JoinGroupInvitation<br />
 * Example: 
 * 
 * <pre>
 * {@code <JoinGroupInvitations>}
 *     {@code <JoinGroupInvitation name=JoinGroupInvitation1 />}
 *     {@code <JoinGroupInvitation name=JoinGroupInvitation2 />}
 * {@code </JoinGroupInvitations>}
 * </pre>
 * <p>
 */ 
@XmlRootElement
public class RestJoinGroupInvitationList extends RestListBinder<RestJoinGroupInvitation, JoinGroupInvitation> {
    /**
     * Creates a RestJoinGroupInvitationList from a {@codePageIterable<JoinGroupInvitation>}
     *
     * @param collection the list of elements from the model
     */
    public RestJoinGroupInvitationList(PageIterable<JoinGroupInvitation> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "joingroupinvitations")
    @XmlElement(name = "joingroupinvitation")
    public RestJoinGroupInvitationList getJoinGroupInvitations() {
        return this;
    }
}

