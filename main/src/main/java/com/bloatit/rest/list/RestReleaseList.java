package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Release;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestRelease;

/**
 * <p>
 * Wraps a list of Release into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Release<br />
 * Example: 
 * 
 * <pre>
 * {@code <Releases>}
 *     {@code <Release name=Release1 />}
 *     {@code <Release name=Release2 />}
 * {@code </Releases>}
 * </pre>
 * <p>
 */ 
@XmlRootElement
public class RestReleaseList extends RestListBinder<RestRelease, Release> {
    /**
     * Creates a RestReleaseList from a {@codePageIterable<Release>}
     *
     * @param collection the list of elements from the model
     */
    public RestReleaseList(PageIterable<Release> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "releases")
    @XmlElement(name = "release")
    public RestReleaseList getReleases() {
        return this;
    }
}

