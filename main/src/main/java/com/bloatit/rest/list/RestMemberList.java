package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Member;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestMember;

/**
 * <p>
 * Wraps a list of Member into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Member<br />
 * Example:
 * 
 * <pre>
 * {@code <Members>}
 *     {@code <Member name=Member1 />}
 *     {@code <Member name=Member2 />}
 * {@code </Members>}
 * </pre>
 * <p>
 */
@XmlRootElement(name = "members")
public class RestMemberList extends RestListBinder<RestMember, Member> {
    @SuppressWarnings("unused")
    private RestMemberList() {
        super();
    }

    /**
     * Creates a RestMemberList from a {@codePageIterable<Member>}
     * 
     * @param collection the list of elements from the model
     */
    public RestMemberList(final PageIterable<Member> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "member")
    public List<RestMember> getMembers() {
        List<RestMember> members = new ArrayList<RestMember>();
        for (RestMember member : this) {
            members.add(member);
        }
        return members;
    }
}
