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

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.model.right.DuplicateDataException;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.ModifySoftwareActionUrl;
import com.bloatit.web.url.ModifySoftwarePageUrl;
import com.bloatit.web.url.SoftwarePageUrl;

/**
 * A response to a form used to modify a software
 */
@ParamContainer("softwares/%software%/domodify")
public final class ModifySoftwareAction extends LoggedElveosAction {
    protected static final String IMAGE_CODE = "image";
    private static final String IMAGE_NAME_CODE = "image/filename";
    private static final String IMAGE_CONTENT_TYPE_CODE = "image/contenttype";
    
    @RequestParam(role = Role.PAGENAME)
    @NonOptional(@tr("You must specify the software to modify"))
    private final Software software;

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

    private final ModifySoftwareActionUrl url;

    public ModifySoftwareAction(final ModifySoftwareActionUrl url) {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.softwareName = url.getSoftwareName();
        this.image = url.getImage();
        this.imageFileName = url.getImageFileName();
        this.imageContentType = url.getImageContentType();
        this.software = url.getSoftware();

    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {

        boolean error = false;

        if (isEmpty(description.trim())) {
            error = true;
            session.notifyError(Context.tr("Cannot delete software's description."));
            url.getDescriptionParameter().addErrorMessage(Context.tr("Cannot delete software's description."));
        }

        if (isEmpty(softwareName.trim())) {
            error = true;
            session.notifyError(Context.tr("Cannot delete software's name."));
            url.getSoftwareNameParameter().addErrorMessage(Context.tr("Cannot delete software's name."));
        } else if (!softwareName.equals(software.getName()) && SoftwareManager.nameExists(softwareName)) {
            error = true;
            final SoftwarePageUrl existingSoftware = new SoftwarePageUrl(SoftwareManager.getByName(softwareName));
            session.notifyError(new HtmlMixedText(Context.tr("This software's name is already used: <0::>."),
                                                  existingSoftware.getHtmlLink(softwareName)));
            url.getSoftwareNameParameter().addErrorMessage(Context.tr("This software's name is already used."));
        }

        if (error) {
            return new ModifySoftwarePageUrl(software);
        }

        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member me) {

        // Description
        if (!isEmpty(description.trim()) && !description.equals(software.getDescription().getTranslation(software.getDescription().getDefaultLanguage()).getText())) {
            try {
                software.getDescription().getTranslation(software.getDescription().getDefaultLanguage()).setText(description, me);
            } catch (final UnauthorizedOperationException e) {
                throw new BadProgrammerException("Fail to update software description");
            }
            session.notifyGood(Context.tr("Software's description changed."));
        }

        // Name
        if (!isEmpty(softwareName.trim()) && !softwareName.equals(software.getName())) {

            try {
                software.setName(softwareName);
                session.notifyGood(Context.tr("Software's name changed."));
            } catch (final DuplicateDataException e) {
                throw new BadProgrammerException("Fail to change the software name", e);
            }

        }

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
            software.setImage(fileImage);
        }

        final SoftwarePageUrl to = new SoftwarePageUrl(software);

        return to;
    }

    @Override
    protected Url doProcessErrors() {
        if (software != null) {
            return new ModifySoftwarePageUrl(software);
        }
        return new IndexPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged to add a software.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSoftwareNameParameter());
    }
}
