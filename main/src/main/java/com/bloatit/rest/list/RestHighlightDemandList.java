package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

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
@XmlRootElement (name = "highlightdemands")
public class RestHighlightDemandList extends RestListBinder<RestHighlightDemand, HighlightDemand> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestHighlightDemandList() {
        super();
    }

    /**
     * Creates a RestHighlightDemandList from a {@codePageIterable<HighlightDemand>}
     *
     * @param collection the list of elements from the model
     */
    public RestHighlightDemandList(PageIterable<HighlightDemand> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "highlightdemand")
    @XmlIDREF
    public List<RestHighlightDemand> getHighlightDemands() {
        List<RestHighlightDemand> highlightdemands = new ArrayList<RestHighlightDemand>();
        for (RestHighlightDemand highlightdemand : this) {
            highlightdemands.add(highlightdemand);
        }
        return highlightdemands;
    }
}

