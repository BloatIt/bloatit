package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Bug;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestBugList;

@XmlRootElement
public class RestBug extends RestElement<Bug> {
    private final Bug model;

    protected RestBug(final Bug model) {
        this.model = model;
    }

    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBug getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "bugs", method = RequestMethod.GET)
    public static RestBugList getAll() {
        // TODO auto generated code
        return null;
    }

    Bug getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
