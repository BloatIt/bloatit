package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Batch;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestBatchList;

@XmlRootElement
public class RestBatch extends RestElement<Batch> {
    private final Batch model;

    protected RestBatch(final Batch model) {
        this.model = model;
    }

    @REST(name = "batchs", method = RequestMethod.GET)
    public static RestBatch getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "batchs", method = RequestMethod.GET)
    public static RestBatchList getAll() {
        // TODO auto generated code
        return null;
    }

    Batch getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
