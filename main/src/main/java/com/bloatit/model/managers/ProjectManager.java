package com.bloatit.model.managers;

import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoProject;
import com.bloatit.model.Project;

public final class ProjectManager {

    private ProjectManager() {
        // Desactivate default ctor
    }

    public static Project getProjectById(final Integer id) {
        return Project.create(DBRequests.getById(DaoProject.class, id));
    }
}
