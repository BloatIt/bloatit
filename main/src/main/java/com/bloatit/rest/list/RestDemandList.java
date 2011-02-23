package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Demand;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestDemand;

@XmlRootElement
public class RestDemandList extends RestListBinder<RestDemand, Demand> {
    public RestDemandList(PageIterable<Demand> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "demands")
    @XmlElement(name = "demand")
    public RestDemandList getDemands() {
        return this;
    }
}

