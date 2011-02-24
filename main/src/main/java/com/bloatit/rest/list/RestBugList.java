package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Bug;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestBug;

/**
 * <p>
 * Wraps a list of Bug into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Bug<br />
 * Example: 
 * 
 * <pre>
 * {@code <Bugs>}
 *     {@code <Bug name=Bug1 />}
 *     {@code <Bug name=Bug2 />}
 * {@code </Bugs>}
 * </pre>
 * <p>
 */ 
@XmlRootElement
public class RestBugList extends RestListBinder<RestBug, Bug> {
    /**
     * Creates a RestBugList from a {@codePageIterable<Bug>}
     *
     * @param collection the list of elements from the model
     */
    public RestBugList(PageIterable<Bug> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "bugs")
    @XmlElement(name = "bug")
    public RestBugList getBugs() {
        return this;
    }
}

