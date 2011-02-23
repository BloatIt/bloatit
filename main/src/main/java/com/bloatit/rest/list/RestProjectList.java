package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Project;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestProject;

/**
 * <p>
 * Wraps a list of Project into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of Project<br />
 * Example:
 * 
 * <pre>
 * {@code <Projects>}
 *     {@code <Project name=Project1 />}
 *     {@code <Project name=Project2 />}
 * {@code </Projects>}
 * </pre>
 * <p>
 */
@XmlRootElement
public class RestProjectList extends RestListBinder<RestProject, Project> {
    /**
     * Creates a RestProjectList from a {@codePageIterable<Project>}
     * 
     * @param collection the list of elements from the model
     */
    public RestProjectList(final PageIterable<Project> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "projects")
    @XmlElement(name = "project")
    public RestProjectList getProjects() {
        return this;
    }
}
