package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

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
@XmlRootElement (name = "descriptions")
public class RestDescriptionList extends RestListBinder<RestDescription, Description> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestDescriptionList() {
        super();
    }

    /**
     * Creates a RestDescriptionList from a {@codePageIterable<Description>}
     *
     * @param collection the list of elements from the model
     */
    public RestDescriptionList(PageIterable<Description> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "description")
    @XmlIDREF
    public List<RestDescription> getDescriptions() {
        List<RestDescription> descriptions = new ArrayList<RestDescription>();
        for (RestDescription description : this) {
            descriptions.add(description);
        }
        return descriptions;
    }
}

