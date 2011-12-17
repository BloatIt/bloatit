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
package com.bloatit.web.linkable.features.create;

import java.util.Locale;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.FileConstraintChecker.SizeUnit;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.form.FormComment;
import com.bloatit.framework.webprocessor.components.form.FormField;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureFactory;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/docreate")
public final class CreateFeatureAction extends UserContentAction {
    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a title"))
    @MinConstraint(min = 10, message = @tr("The title must have at least %constraint% chars."))
    @MaxConstraint(max = 80, message = @tr("The title must be %constraint% chars length max."))
    @FormField(label = @tr("Title"), isShort = false, autocomplete = false)
    @FormComment(@tr("The title length must be at least 10 chars long."))
    private final String description;

    @NonOptional(@tr("You forgot to write a description"))
    @MinConstraint(min = 10, message = @tr("The description must have at least %constraint% chars."))
    @MaxConstraint(max = 800000, message = @tr("The description must be %constraint% chars length max."))
    @RequestParam(role = Role.POST)
    @FormField(label = @tr("Description"), isShort = false)
    @FormComment(@tr("Try to be the most specific possible."))
    private final String specification;

    @Optional
    @RequestParam(role = Role.POST)
    @FormField(label = @tr("Software"))
    private final Software software;

    @Optional
    @RequestParam(role = Role.POST)
    private final String newSoftwareName;

    @SuppressWarnings("unused")
    @RequestParam(role = Role.POST)
    @Optional
    private final Boolean newSoftware;

    private final CreateFeatureActionUrl url;

    public CreateFeatureAction(final CreateFeatureActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;

        this.description = url.getDescription();
        this.specification = url.getSpecification();
        this.software = url.getSoftware();
        this.newSoftwareName = url.getNewSoftwareName();
        this.newSoftware = url.getNewSoftware();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        if (getLocale() == null) {
            session.notifyError(Context.tr("You have to specify a valid language."));
            return new CreateFeaturePageUrl();
        }

        if (software == null && newSoftwareName != null && newSoftwareName.equals("--invalid--")) {
            session.notifyError(Context.tr("You have to specify a valid software."));
            return new CreateFeaturePageUrl();
        }
        return NO_ERROR;
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) throws UnauthorizedOperationException {
        Software softwareToUse = software;

        if (software == null && newSoftwareName != null && !newSoftwareName.isEmpty()) {
            try {
                softwareToUse = new Software(newSoftwareName, me, Locale.ENGLISH, "No description yet.");
            } catch (UniqueNameExpectedException e) {
                softwareToUse = SoftwareManager.getByName(newSoftwareName);
            }

        }

        final Feature feature = FeatureFactory.createFeature(me, asTeam, Language.fromLocale(getLocale()), description, specification, softwareToUse);
        propagateAttachedFileIfPossible(feature);
        return new FeaturePageUrl(feature, FeatureTabKey.description);
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
        session.addParameter(url.getNewSoftwareNameParameter());
        session.addParameter(url.getNewSoftwareParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        return new FileConstraintChecker(filename).isFileSmaller(CreateFeaturePage.FILE_MAX_SIZE_MIO, SizeUnit.MBYTE);
    }
}
