package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Batch;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestBatch;

@XmlRootElement
public class RestBatchList extends RestListBinder<RestBatch, Batch> {
    public RestBatchList(PageIterable<Batch> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "batchs")
    @XmlElement(name = "batch")
    public RestBatchList getBatchs() {
        return this;
    }
}

