package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestJoinGroupInvitationList;

@XmlRootElement
public class RestJoinGroupInvitation extends RestElement<JoinGroupInvitation> {
    private final JoinGroupInvitation model;

    protected RestJoinGroupInvitation(final JoinGroupInvitation model) {
        this.model = model;
    }

    /**
     * @param id
     * @return
     */
    @REST(name = "joingroupinvitations", method = RequestMethod.GET)
    public static RestJoinGroupInvitation getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "joingroupinvitations", method = RequestMethod.GET)
    public static RestJoinGroupInvitationList getAll() {
        // TODO auto generated code
        return null;
    }

    JoinGroupInvitation getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
