package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestGroup;

/**
 * <p>
 * Wraps a list of Group into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Group<br />
 * Example:
 * 
 * <pre>
 * {@code <Groups>}
 *     {@code <Group name=Group1 />}
 *     {@code <Group name=Group2 />}
 * {@code </Groups>}
 * </pre>
 * <p>
 */
@XmlRootElement
public class RestGroupList extends RestListBinder<RestGroup, Group> {
    /**
     * Creates a RestGroupList from a {@codePageIterable<Group>}
     * 
     * @param collection the list of elements from the model
     */
    public RestGroupList(final PageIterable<Group> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    public RestGroupList getGroups() {
        return this;
    }
}
