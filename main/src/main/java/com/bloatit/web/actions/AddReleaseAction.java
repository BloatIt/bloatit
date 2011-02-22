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
 * A response to a form used to create a new idea
 */
@ParamContainer("release/doadd")
public final class AddReleaseAction extends LoggedAction {

    public static final String SHORT_DESCRIPTION_CODE = "bloatit_project_short_description";
    public static final String DESCRIPTION_CODE = "bloatit_project_description";
    public static final String PROJECT_NAME_CODE = "bloatit_idea_project";
    public static final String IMAGE_CODE = "attachedfile";
    public static final String IMAGE_NAME_CODE = "attachedfile/filename";
    public static final String IMAGE_CONTENT_TYPE_CODE = "attachedfile/contenttype";
    public static final String LANGUAGE_CODE = "bloatit_idea_lang";

    @RequestParam(name = SHORT_DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint(min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."), optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String shortDescription;

    @RequestParam(name = IMAGE_CODE, role = Role.POST)
    private final String attachedfile;

    @RequestParam(name = IMAGE_NAME_CODE, role = Role.POST)
    private final String attachedfileFileName;

    @RequestParam(name = IMAGE_CONTENT_TYPE_CODE, role = Role.POST)
    private final String attachedfileContentType;

    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;

    @RequestParam(name = LANGUAGE_CODE)
    private final Batch batch;

    private final AddReleaseActionUrl url;

    public AddReleaseAction(final AddReleaseActionUrl url) {
        super(url);
        this.url = url;

        this.shortDescription = url.getShortDescription();
        this.attachedfile = url.getAttachedfile();
        this.attachedfileContentType = url.getAttachedfileContentType();
        this.attachedfileFileName = url.getAttachedfileFileName();
        this.lang = url.getLang();
        this.batch = url.getBatch();

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
        batch.addRelease(shortDescription, langLocale, fileImage);
        return session.getLastStablePage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a release.");
    }
    
    @Override
    protected Url doProcessErrors() {
        return session.pickPreferredPage();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getShortDescriptionParameter());
        session.addParameter(url.getAttachedfileContentTypeParameter());
        session.addParameter(url.getAttachedfileFileNameParameter());
        session.addParameter(url.getAttachedfileParameter());
        session.addParameter(url.getLangParameter());
    }
}
