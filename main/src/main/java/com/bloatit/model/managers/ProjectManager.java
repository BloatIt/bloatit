package com.bloatit.model.managers;

import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoProject;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Project;
import com.bloatit.model.lists.ProjectList;

public final class ProjectManager {

    private ProjectManager() {
        // Desactivate default ctor
    }

    public static Project getProjectById(final Integer id) {
        return Project.create(DBRequests.getById(DaoProject.class, id));
    }

    public static PageIterable<Project> getProjects() {
        return new ProjectList(DBRequests.getAll(DaoProject.class));
    }
}
