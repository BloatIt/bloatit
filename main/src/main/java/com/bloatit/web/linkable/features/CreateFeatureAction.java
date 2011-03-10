/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.features;

import java.util.Locale;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureFactory;
import com.bloatit.model.Project;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.LoginPageUrl;

/**
 * A response to a form used to create a new idea
 */
@ParamContainer("idea/docreate")
public final class CreateFeatureAction extends Action {

    public static final String DESCRIPTION_CODE = "description";
    public static final String SPECIFICATION_CODE = "specification";
    public static final String PROJECT_CODE = "project";
    public static final String LANGUAGE_CODE = "bloatit_idea_lang";

    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint(max = "80", maxErrorMsg = @tr("The title must be 80 chars length max."), //
    min = "10", minErrorMsg = @tr("The title must have at least 10 chars."), optionalErrorMsg = @tr("Error you forgot to write a title"))
    private final String description;

    @RequestParam(name = SPECIFICATION_CODE, role = Role.POST)
    private final String specification;

    @RequestParam(name = PROJECT_CODE, role = Role.POST)
    private final Project project;

    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final CreateFeatureActionUrl url;

    public CreateFeatureAction(final CreateFeatureActionUrl url) {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.specification = url.getSpecification();
        this.project = url.getProject();
        this.lang = url.getLang();

    }

    @Override
    protected Url doProcess() {
        session.notifyList(url.getMessages());
        if (!FeatureManager.canCreate(session.getAuthToken())) {
            session.notifyError(Context.tr("You must be logged in to create an idea."));
            return new LoginPageUrl();
        }
        final Locale langLocale = new Locale(lang);
        final Feature d = FeatureFactory.createFeature(session.getAuthToken().getMember(), langLocale, description, specification, project);

        final FeaturePageUrl to = new FeaturePageUrl(d);

        return to;
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());

        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSpecificationParameter());
        session.addParameter(url.getProjectParameter());
        session.addParameter(url.getLangParameter());

        return new CreateFeaturePageUrl();
    }
}
