/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import java.util.Locale;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Project;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.url.AddProjectActionUrl;
import com.bloatit.web.url.AddProjectPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.ProjectPageUrl;

/**
 * A response to a form used to create a new idea
 */
@ParamContainer("project/doadd")
public final class AddProjectAction extends Action {

    public static final String SHORT_DESCRIPTION_CODE = "bloatit_project_short_description";
    public static final String DESCRIPTION_CODE = "bloatit_project_description";
    public static final String PROJECT_NAME_CODE = "bloatit_idea_project";
    public static final String IMAGE_CODE = "image";
    public static final String LANGUAGE_CODE = "bloatit_idea_lang";

    @RequestParam(name = SHORT_DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint(max = "120",
                     maxErrorMsg = @tr("The short description must be 12 chars length max."), //
                     min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."),
                     optionalErrorMsg = @tr("Error you forgot to write a short description"))
    private final String shortDescription;

    @RequestParam(name = DESCRIPTION_CODE, defaultValue = "", role = Role.POST)
    private final String description;

    @RequestParam(name = PROJECT_NAME_CODE, role = Role.POST)
    private final String projectName;

    @RequestParam(name = IMAGE_CODE, role = Role.POST)
    private final String image;


    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final AddProjectActionUrl url;

    public AddProjectAction(final AddProjectActionUrl url) {
        super(url);
        this.url = url;

        this.shortDescription = url.getShortDescription();
        this.description = url.getDescription();
        this.projectName = url.getProjectName();
        this.lang = url.getLang();
        this.image = url.getImage();

    }

    @Override
    protected Url doProcess() throws RedirectException {
        session.notifyList(url.getMessages());
        if (!DemandManager.canCreate(session.getAuthToken())) {
            session.notifyError(Context.tr("You must be logged in to add a project."));
            return new LoginPageUrl();
        }
        final Locale langLocale = new Locale(lang);
        // TODO make it work


        FileMetadata fileImage = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(), image);

        final Project p = new Project(projectName, session.getAuthToken().getMember(), langLocale, shortDescription, description, fileImage);

        p.authenticate(session.getAuthToken());

        final ProjectPageUrl to = new ProjectPageUrl(p);

        return to;
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());

        session.addParameter(SHORT_DESCRIPTION_CODE, shortDescription);
        session.addParameter(DESCRIPTION_CODE, description);
        session.addParameter(PROJECT_NAME_CODE, projectName);
        session.addParameter(url.getLangParameter());

        return new AddProjectPageUrl();
    }
}
