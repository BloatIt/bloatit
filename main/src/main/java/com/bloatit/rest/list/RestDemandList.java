package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Demand;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestDemand;

/**
 * <p>
 * Wraps a list of Demand into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Demand<br />
 * Example: 
 * 
 * <pre>
 * {@code <Demands>}
 *     {@code <Demand name=Demand1 />}
 *     {@code <Demand name=Demand2 />}
 * {@code </Demands>}
 * </pre>
 * <p>
 */ 
@XmlRootElement (name = "demands")
public class RestDemandList extends RestListBinder<RestDemand, Demand> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestDemandList() {
        super();
    }

    /**
     * Creates a RestDemandList from a {@codePageIterable<Demand>}
     *
     * @param collection the list of elements from the model
     */
    public RestDemandList(PageIterable<Demand> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "demand")
    @XmlIDREF
    List<RestDemand> getDemandsID() {
        List<RestDemand> demands = new ArrayList<RestDemand>();
        for (RestDemand demand : this) {
            demands.add(demand);
        }
        return demands;
    }
}

