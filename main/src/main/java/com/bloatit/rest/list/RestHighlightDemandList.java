package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.HighlightDemand;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestHighlightDemand;

/**
 * <p>
 * Wraps a list of HighlightDemand into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of HighlightDemand<br />
 * Example:
 * 
 * <pre>
 * {@code <HighlightDemands>}
 *     {@code <HighlightDemand name=HighlightDemand1 />}
 *     {@code <HighlightDemand name=HighlightDemand2 />}
 * {@code </HighlightDemands>}
 * </pre>
 * <p>
 */
@XmlRootElement
public class RestHighlightDemandList extends RestListBinder<RestHighlightDemand, HighlightDemand> {
    /**
     * Creates a RestHighlightDemandList from a
     * {@codePageIterable<HighlightDemand>}
     * 
     * @param collection the list of elements from the model
     */
    public RestHighlightDemandList(final PageIterable<HighlightDemand> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "highlightdemands")
    @XmlElement(name = "highlightdemand")
    public RestHighlightDemandList getHighlightDemands() {
        return this;
    }
}
