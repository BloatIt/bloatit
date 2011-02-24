package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

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
@XmlRootElement(name = "projects")
public class RestProjectList extends RestListBinder<RestProject, Project> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestProjectList() {
        super();
    }

    /**
     * Creates a RestProjectList from a {@codePageIterable<Project>}
     *
     * @param collection the list of elements from the model
     */
    public RestProjectList(PageIterable<Project> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "project")
    @XmlIDREF
    public List<RestProject> getProjects() {
        List<RestProject> projects = new ArrayList<RestProject>();
        for (RestProject project : this) {
            projects.add(project);
        }
        return projects;
    }
}
