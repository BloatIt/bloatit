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
package com.bloatit.web.actions;

import java.util.Locale;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Batch;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.managers.FileMetadataManager;
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

    @RequestParam(name = "attachedfile/filename", role = Role.POST)
    private final String attachedfileFileName;

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
    private final Batch batch;

    private final AddReleaseActionUrl url;

    public AddReleaseAction(final AddReleaseActionUrl url) {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.attachedfile = url.getAttachedfile();
        this.attachedfileContentType = url.getAttachedfileContentType();
        this.attachedfileFileName = url.getAttachedfileFileName();
        this.lang = url.getLang();
        this.batch = url.getBatch();
        this.version = url.getVersion();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        if (!batch.getOffer().getAuthor().equals(authenticatedMember)) {
            return session.pickPreferredPage();
        }

        final Locale langLocale = new Locale(lang);
        final FileMetadata fileImage = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                              attachedfile,
                                                                              attachedfileFileName,
                                                                              null);
        batch.addRelease(description, version, langLocale, fileImage);
        session.notifyGood(Context.tr("Release created successfuly !"));
        return session.getLastStablePage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a release.");
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
        return session.pickPreferredPage();
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
