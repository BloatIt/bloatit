package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Release;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestReleaseList;

@XmlRootElement
public class RestRelease extends RestElement<Release> {
    private final Release model;

    protected RestRelease(final Release model) {
        this.model = model;
    }

    @REST(name = "releases", method = RequestMethod.GET)
    public static RestRelease getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "releases", method = RequestMethod.GET)
    public static RestReleaseList getAll() {
        // TODO auto generated code
        return null;
    }

    Release getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
