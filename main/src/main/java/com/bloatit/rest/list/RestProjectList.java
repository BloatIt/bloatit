package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Project;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestProject;

@XmlRootElement
public class RestProjectList extends RestListBinder<RestProject, Project> {
    public RestProjectList(PageIterable<Project> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "projects")
    @XmlElement(name = "project")
    public RestProjectList getProjects() {
        return this;
    }
}

