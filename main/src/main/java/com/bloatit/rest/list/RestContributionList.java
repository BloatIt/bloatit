package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Contribution;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestContribution;

@XmlRootElement
public class RestContributionList extends RestListBinder<RestContribution, Contribution> {
    public RestContributionList(PageIterable<Contribution> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "contributions")
    @XmlElement(name = "contribution")
    public RestContributionList getContributions() {
        return this;
    }
}

