package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Project;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestProjectList;

@XmlRootElement
public class RestProject extends RestElement<Project> {
    private final Project model;

    protected RestProject(final Project model) {
        this.model = model;
    }

    @REST(name = "projects", method = RequestMethod.GET)
    public static RestProject getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "projects", method = RequestMethod.GET)
    public static RestProjectList getAll() {
        // TODO auto generated code
        return null;
    }

    Project getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
