package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestJoinGroupInvitation;

@XmlRootElement
public class RestJoinGroupInvitationList extends RestListBinder<RestJoinGroupInvitation, JoinGroupInvitation> {
    public RestJoinGroupInvitationList(PageIterable<JoinGroupInvitation> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "joingroupinvitations")
    @XmlElement(name = "joingroupinvitation")
    public RestJoinGroupInvitationList getJoinGroupInvitations() {
        return this;
    }
}

