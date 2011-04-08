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
package com.bloatit.web.linkable.bugs;

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Milestone;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/bug/doreport")
public final class ReportBugAction extends Action {

    public static final String BUG_TITLE = "bug_title";
    public static final String BUG_DESCRIPTION = "bug_description";
    public static final String BUG_LEVEL = "bug_level";
    public static final String BUG_BATCH = "bug_milestone";
    public static final String LANGUAGE_CODE = "bug_description_language";
    public static final String ATTACHEMENT_CODE = "attachement";
    public static final String ATTACHEMENT_NAME_CODE = "attachement/filename";
    public static final String ATTACHEMENT_CONTENT_TYPE_CODE = "attachement/contenttype";
    public static final String ATTACHEMENT_DESCRIPTION_CODE = "attachement_description";

    @RequestParam(name = BUG_TITLE, role = Role.POST)
    @ParamConstraint(max = "120", maxErrorMsg = @tr("The short description must be 120 chars length max."), //
    min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."), optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String title;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug description"))
    @RequestParam(name = BUG_DESCRIPTION, role = Role.POST)
    private final String description;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a description language"))
    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private final String lang;
    private final ReportBugActionUrl url;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug level"))
    @RequestParam(name = BUG_LEVEL, suggestedValue = "MINOR", role = Role.POST)
    private final BindedLevel level;

    @ParamConstraint(optionalErrorMsg = @tr("A new bug must be linked to a milestone"))
    @RequestParam(name = BUG_BATCH, role = Role.GET)
    private final Milestone milestone;

    @Optional
    @RequestParam(name = ATTACHEMENT_CODE, role = Role.POST)
    private final String attachement;

    @Optional
    @RequestParam(name = ATTACHEMENT_NAME_CODE, role = Role.POST)
    private final String attachementFileName;

    @Optional
    @RequestParam(name = ATTACHEMENT_DESCRIPTION_CODE, role = Role.POST)
    private final String attachementDescription;

    @SuppressWarnings("unused")
    @Optional
    @RequestParam(name = ATTACHEMENT_CONTENT_TYPE_CODE, role = Role.POST)
    private final String attachementContentType;

    public ReportBugAction(final ReportBugActionUrl url) {
        super(url);
        this.url = url;

        this.title = url.getTitle();
        this.description = url.getDescription();
        this.lang = url.getLang();
        this.level = url.getLevel();
        this.milestone = url.getMilestone();
        this.attachement = url.getAttachement();
        this.attachementFileName = url.getAttachementFileName();
        this.attachementContentType = url.getAttachementContentType();
        this.attachementDescription = url.getAttachementDescription();

    }

    @Override
    protected Url doProcess() {
        session.notifyList(url.getMessages());
        if (!FeatureManager.canCreate(session.getAuthToken())) {
            // TODO: use BugManager and not FeatureManager here
            session.notifyError(Context.tr("You must be logged in to report a bug."));
            return new LoginPageUrl();
        }

        if (attachement != null && (attachementDescription == null || attachementDescription.isEmpty())) {
            session.notifyError(Context.tr("You must enter a description of the attachement if you add an attachement."));
            return redirectWithError();
        }

        final Locale langLocale = new Locale(lang);

        final Bug bug = milestone.addBug(session.getAuthToken().getMember(), title, description, langLocale, level.getLevel());

        if (attachement != null) {
            final FileMetadata attachementFileMedatata = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                                                attachement,
                                                                                                attachementFileName,
                                                                                                attachementDescription);

            try {
                bug.addFile(attachementFileMedatata);
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("Fail to add the attachement to the bug report."));
                throw new ShallNotPassException("Fail to add an attachement to the new bug report.",e);
            }
        }

        final BugPageUrl to = new BugPageUrl(bug);

        return to;
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());

        if (milestone != null) {
            return redirectWithError();
        }
        return new PageNotFoundUrl();
    }

    public Url redirectWithError() {
        session.addParameter(url.getTitleParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getMilestoneParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getLangParameter());
        session.addParameter(url.getAttachementDescriptionParameter());
        return new ReportBugPageUrl(milestone.getOffer());
    }

}
