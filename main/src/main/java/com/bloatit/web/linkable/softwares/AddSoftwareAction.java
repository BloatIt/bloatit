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
package com.bloatit.web.linkable.softwares;

import java.util.Locale;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.AddSoftwareActionUrl;
import com.bloatit.web.url.AddSoftwarePageUrl;
import com.bloatit.web.url.SoftwarePageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("software/doadd")
public final class AddSoftwareAction extends LoggedAction {

    public static final String SHORT_DESCRIPTION_CODE = "bloatit_software_short_description";
    public static final String DESCRIPTION_CODE = "feature_software_description";
    public static final String SOFTWARE_NAME_CODE = "feature_software";
    public static final String IMAGE_CODE = "image";
    public static final String IMAGE_NAME_CODE = "image/filename";
    public static final String IMAGE_CONTENT_TYPE_CODE = "image/contenttype";
    public static final String LANGUAGE_CODE = "feature_lang";

    @RequestParam(name = SHORT_DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint(max = "120",
                     maxErrorMsg = @tr("The short description must be 120 chars length max."), //
                     min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."),
                     optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String shortDescription;

    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint
    @Optional
    private final String description;

    @RequestParam(name = SOFTWARE_NAME_CODE, role = Role.POST)
    @ParamConstraint(max = "100",
                     maxErrorMsg = @tr("The software name must be 1OO chars length max."), //
                     min = "3", minErrorMsg = @tr("The software name must have at least 3 chars."),
                     optionalErrorMsg = @tr("The software name is requiered."))
    private final String softwareName;

    @Optional
    @RequestParam(name = IMAGE_CODE, role = Role.POST)
    private final String image;

    @Optional
    @RequestParam(name = IMAGE_NAME_CODE, role = Role.POST)
    private final String imageFileName;

    @SuppressWarnings("unused")
    @Optional
    @RequestParam(name = IMAGE_CONTENT_TYPE_CODE, role = Role.POST)
    private final String imageContentType;

    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final AddSoftwareActionUrl url;

    public AddSoftwareAction(final AddSoftwareActionUrl url) {
        super(url);
        this.url = url;

        this.shortDescription = url.getShortDescription();
        this.description = url.getDescription();
        this.softwareName = url.getSoftwareName();
        this.lang = url.getLang();
        this.image = url.getImage();
        this.imageFileName = url.getImageFileName();
        this.imageContentType = url.getImageContentType();

    }

    @Override
    protected Url doCheckRightsAndEverything(Member authenticatedMember) {
        if (!FeatureManager.canCreate(session.getAuthToken())) {
            // TODO: use SoftwareManager and not FeatureManager here
            session.notifyError(Context.tr("You must be logged in to add a software."));
            return new AddSoftwarePageUrl();
        }
        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member authenticatedMember) {
        final Locale langLocale = new Locale(lang);

        final Software p = new Software(softwareName, session.getAuthToken().getMember(), langLocale, shortDescription, description);

        if (image != null) {
            final FileMetadata fileImage = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                                  image,
                                                                                  imageFileName,
                                                                                  "Image for the software '" + softwareName + "'");
            p.setImage(fileImage);
        }

        final SoftwarePageUrl to = new SoftwarePageUrl(p);

        return to;
    }

    @Override
    protected Url doProcessErrors() {
        return new AddSoftwarePageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged to add a software.");
    }

    @Override
    protected void transmitParameters() {
        // TODO make sure all the parameters are transmitted
        session.addParameter(url.getShortDescriptionParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSoftwareNameParameter());
        session.addParameter(url.getLangParameter());
    }
}
