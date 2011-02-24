package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Kudos;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestKudos;

/**
 * <p>
 * Wraps a list of Kudos into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Kudos<br />
 * Example: 
 * 
 * <pre>
 * {@code <Kudoss>}
 *     {@code <Kudos name=Kudos1 />}
 *     {@code <Kudos name=Kudos2 />}
 * {@code </Kudoss>}
 * </pre>
 * <p>
 */ 
@XmlRootElement (name = "kudoss")
public class RestKudosList extends RestListBinder<RestKudos, Kudos> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestKudosList() {
        super();
    }

    /**
     * Creates a RestKudosList from a {@codePageIterable<Kudos>}
     *
     * @param collection the list of elements from the model
     */
    public RestKudosList(PageIterable<Kudos> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "kudos")
    @XmlIDREF
    public List<RestKudos> getKudoss() {
        List<RestKudos> kudoss = new ArrayList<RestKudos>();
        for (RestKudos kudos : this) {
            kudoss.add(kudos);
        }
        return kudoss;
    }
}

