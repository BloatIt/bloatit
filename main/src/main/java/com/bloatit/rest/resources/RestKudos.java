package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Kudos;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestKudosList;

@XmlRootElement
public class RestKudos extends RestElement<Kudos> {
    private final Kudos model;

    protected RestKudos(final Kudos model) {
        this.model = model;
    }

    @REST(name = "kudoss", method = RequestMethod.GET)
    public static RestKudos getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "kudoss", method = RequestMethod.GET)
    public static RestKudosList getAll() {
        // TODO auto generated code
        return null;
    }

    Kudos getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
