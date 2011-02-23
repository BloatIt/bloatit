package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Member;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestMember;

@XmlRootElement
public class RestMemberList extends RestListBinder<RestMember, Member> {
    public RestMemberList(PageIterable<Member> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "members")
    @XmlElement(name = "member")
    public RestMemberList getMembers() {
        return this;
    }
}

