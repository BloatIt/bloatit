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

import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureFactory;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.linkable.usercontent.CreateUserContentAction;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/docreate")
public final class CreateFeatureAction extends CreateUserContentAction {
    @RequestParam(role = Role.POST)
    @ParamConstraint(max = "80", maxErrorMsg = @tr("The title must be 80 chars length max."), //
                     min = "10", minErrorMsg = @tr("The title must have at least 10 chars."), //
                     optionalErrorMsg = @tr("Error you forgot to write a title"))
    private final String description;

    @RequestParam(role = Role.POST)
    private final String specification;

    @RequestParam(role = Role.POST)
    private final Software software;

    private final CreateFeatureActionUrl url;

    public CreateFeatureAction(final CreateFeatureActionUrl url) {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.specification = url.getSpecification();
        this.software = url.getSoftware();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member authenticatedMember) {
        if (!FeatureManager.canCreate(session.getAuthToken())) {
            session.notifyError(Context.tr("You are not authorized to create a feature."));
            return new CreateFeaturePageUrl();
        }
        if (getLocale() == null) {
            session.notifyError(Context.tr("You have to specify a valid language."));
            return new CreateFeaturePageUrl();
        }
        return NO_ERROR;
    }

    @Override
    public Url doDoProcessRestricted(final Member authenticatedMember) {
        final Feature feature = FeatureFactory.createFeature(authenticatedMember, getLocale(), description, specification, software);
        propagateAsTeamIfPossible(feature);
        propagateAttachedFileIfPossible(feature);
        return new FeaturePageUrl(feature);
    }

    @Override
    protected Url doProcessErrors() {
        return new CreateFeaturePageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return "You have to be logged to create a new feature request.";
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSpecificationParameter());
        session.addParameter(url.getSoftwareParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        // TODO verify the file.
        return true;
    }

}
