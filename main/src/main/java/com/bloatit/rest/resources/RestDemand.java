package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Demand;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestDemandList;

@XmlRootElement
public class RestDemand extends RestElement<Demand> {
    private final Demand model;

    protected RestDemand(final Demand model) {
        this.model = model;
    }

    @REST(name = "demands", method = RequestMethod.GET)
    public static RestDemand getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "demands", method = RequestMethod.GET)
    public static RestDemandList getAll() {
        // TODO auto generated code
        return null;
    }

    Demand getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
