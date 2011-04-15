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
package com.bloatit.web.linkable.release;

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.FileConstraintChecker.SizeUnit;
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
import com.bloatit.model.Milestone;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.AddReleaseActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("release/doadd")
public final class AddReleaseAction extends LoggedAction {

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "10", minErrorMsg = @tr("The description must have at least 10 chars."), //
    optionalErrorMsg = @tr("You forgot to write a description"))
    private final String description;

    @ParamConstraint(optionalErrorMsg = @tr("You forgot the attachment file."))
    @RequestParam(role = Role.POST)
    private final String attachedfile;

    @Optional
    @RequestParam(name = "attachedfile/filename", role = Role.POST)
    private final String attachedfileFileName;

    @SuppressWarnings("unused")
    @Optional
    @RequestParam(name = "attachedfile/contenttype", role = Role.POST)
    private final String attachedfileContentType;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "2", minErrorMsg = @tr("Are you sure this is a ISO language code"), //
    max = "2", maxErrorMsg = @tr("Are you sure this is a ISO language code"), //
    optionalErrorMsg = @tr("Language code is not optional. You are messing up with our web site..."))
    private final String lang;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "1", minErrorMsg = @tr("The version should be something like ''1.2.3''."), //
    optionalErrorMsg = @tr("You forgot to write a version."))
    private final String version;

    @RequestParam
    private final Milestone milestone;

    private final AddReleaseActionUrl url;

    public AddReleaseAction(final AddReleaseActionUrl url) {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.attachedfile = url.getAttachedfile();
        this.attachedfileContentType = url.getAttachedfileContentType();
        this.attachedfileFileName = url.getAttachedfileFileName();
        this.lang = url.getLang();
        this.milestone = url.getMilestone();
        this.version = url.getVersion();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        // TODO: Verify user right
        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        final Locale langLocale = new Locale(lang);

        final FileConstraintChecker fcc = new FileConstraintChecker(attachedfile);
        if (!fcc.exists() || !fcc.isFileSmaller(1, SizeUnit.GBYTE)) {
            session.notifyBad(Context.tr("File format error: Your file is too big."));
            return Context.getSession().pickPreferredPage();
        }

        final FileMetadata fileImage = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                              attachedfile,
                                                                              attachedfileFileName,
                                                                              null);
        try {
            milestone.addRelease(description, version, langLocale, fileImage);
            session.notifyGood(Context.tr("Release created successfuly!"));

        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("Failed to create the release."));
            new ShallNotPassException("Fail to create a release.", e);
        }
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a release.");
    }

    @Override
    protected Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getAttachedfileContentTypeParameter());
        session.addParameter(url.getAttachedfileFileNameParameter());
        session.addParameter(url.getAttachedfileParameter());
        session.addParameter(url.getLangParameter());
    }
}
