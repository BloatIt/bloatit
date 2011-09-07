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

import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedElveosAction;
import com.bloatit.web.url.CreateSoftwareActionUrl;
import com.bloatit.web.url.CreateSoftwarePageUrl;
import com.bloatit.web.url.SoftwarePageUrl;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("software/docreate")
public final class CreateSoftwareAction extends LoggedElveosAction {
    protected static final String IMAGE_CODE = "image";
    private static final String IMAGE_NAME_CODE = "image/filename";
    private static final String IMAGE_CONTENT_TYPE_CODE = "image/contenttype";
    private static final String LANGUAGE_CODE = "feature_lang";

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a description"))
    @MinConstraint(min = 10, message = @tr("The description must have at least %constraint% chars."))
    private final String description;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("The software name is required."))
    @MinConstraint(min = 3, message = @tr("The software name must have at least %constraint% chars."))
    @MaxConstraint(max = 64, message = @tr("The software name must be %constraint% chars length max."))
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
    private final CreateSoftwareActionUrl url;

    public CreateSoftwareAction(final CreateSoftwareActionUrl url) {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.softwareName = url.getSoftwareName();
        this.lang = url.getLang();
        this.image = url.getImage();
        this.imageFileName = url.getImageFileName();
        this.imageContentType = url.getImageContentType();

    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        final Locale langLocale = new Locale(lang);

        try {
            Software p = new Software(softwareName, me, langLocale, description);

            if (image != null) {
                final FileConstraintChecker fcc = new FileConstraintChecker(image);
                if (fcc.isImageAvatar() != null) {
                    for (final String message : fcc.isImageAvatar()) {
                        session.notifyWarning(message);
                    }
                    return Context.getSession().pickPreferredPage();
                }
                final FileMetadata fileImage = FileMetadataManager.createFromTempFile(me, null, image, imageFileName, "Image for the software '"
                        + softwareName + "'");
                p.setImage(fileImage);
            }

            final SoftwarePageUrl to = new SoftwarePageUrl(p);

            return to;
        } catch (UniqueNameExpectedException e) {
            Context.getSession().notifyError(Context.tr("A software with the same name already exists."));
            return doProcessErrors();
        }
    }

    @Override
    protected Url doProcessErrors() {
        return new CreateSoftwarePageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged to add a software.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSoftwareNameParameter());
        session.addParameter(url.getLangParameter());
    }
}
