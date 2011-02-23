package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Description;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestDescription;

/**
 * <p>
 * Wraps a list of Description into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Description<br />
 * Example:
 * 
 * <pre>
 * {@code <Descriptions>}
 *     {@code <Description name=Description1 />}
 *     {@code <Description name=Description2 />}
 * {@code </Descriptions>}
 * </pre>
 * <p>
 */
@XmlRootElement
public class RestDescriptionList extends RestListBinder<RestDescription, Description> {
    /**
     * Creates a RestDescriptionList from a {@codePageIterable<Description>}
     * 
     * @param collection the list of elements from the model
     */
    public RestDescriptionList(final PageIterable<Description> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "descriptions")
    @XmlElement(name = "description")
    public RestDescriptionList getDescriptions() {
        return this;
    }
}
