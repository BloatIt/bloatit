package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Member;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestMemberList;

@XmlRootElement
public class RestMember extends RestElement<Member> {
    private final Member model;

    protected RestMember(final Member model) {
        this.model = model;
    }

    @REST(name = "members", method = RequestMethod.GET)
    public static RestMember getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "members", method = RequestMethod.GET)
    public static RestMemberList getAll() {
        // TODO auto generated code
        return null;
    }

    Member getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
