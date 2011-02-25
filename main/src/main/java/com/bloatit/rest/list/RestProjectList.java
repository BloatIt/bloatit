/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
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
