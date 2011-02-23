package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Description;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestDescriptionList;

@XmlRootElement
public class RestDescription extends RestElement<Description> {
    private final Description model;

    protected RestDescription(final Description model) {
        this.model = model;
    }

    @REST(name = "descriptions", method = RequestMethod.GET)
    public static RestDescription getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "descriptions", method = RequestMethod.GET)
    public static RestDescriptionList getAll() {
        // TODO auto generated code
        return null;
    }

    Description getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
