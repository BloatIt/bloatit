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
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.FileConstraintChecker.SizeUnit;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Bug;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/bug/doreport")
public final class ReportBugAction extends LoggedAction {

    public static final String BUG_TITLE = "bug_title";
    public static final String BUG_DESCRIPTION = "bug_description";
    public static final String BUG_LEVEL = "bug_level";
    public static final String BUG_BATCH = "bug_milestone";
    public static final String LANGUAGE_CODE = "bug_description_language";
    public static final String ATTACHEMENT_CODE = "attachment";
    public static final String ATTACHEMENT_NAME_CODE = "attachment/filename";
    public static final String ATTACHEMENT_CONTENT_TYPE_CODE = "attachment/contenttype";
    public static final String ATTACHEMENT_DESCRIPTION_CODE = "attachment_description";

    @RequestParam(name = BUG_TITLE, role = Role.POST)
    @ParamConstraint(max = "120",
                     maxErrorMsg = @tr("The short description must be 120 chars length max."), //
                     min = "10", minErrorMsg = @tr("The short description must have at least 10 chars."),
                     optionalErrorMsg = @tr("You forgot to write a short description"))
    private final String title;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug description"), min = "10", minErrorMsg = @tr("The description must have at least 10 chars."))
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
    private final String attachment;

    @Optional
    @RequestParam(name = ATTACHEMENT_NAME_CODE, role = Role.POST)
    private final String attachmentFileName;

    @Optional
    @RequestParam(name = ATTACHEMENT_DESCRIPTION_CODE, role = Role.POST)
    private final String attachmentDescription;

    @SuppressWarnings("unused")
    @Optional
    @RequestParam(name = ATTACHEMENT_CONTENT_TYPE_CODE, role = Role.POST)
    private final String attachmentContentType;

    public ReportBugAction(final ReportBugActionUrl url) {
        super(url);
        this.url = url;

        this.title = url.getTitle();
        this.description = url.getDescription();
        this.lang = url.getLang();
        this.level = url.getLevel();
        this.milestone = url.getMilestone();
        this.attachment = url.getAttachment();
        this.attachmentFileName = url.getAttachmentFileName();
        this.attachmentContentType = url.getAttachmentContentType();
        this.attachmentDescription = url.getAttachmentDescription();

    }

    @Override
    public Url doProcessRestricted(final Member authenticatedMember) {
        final Locale langLocale = new Locale(lang);
        final Bug bug = milestone.addBug(authenticatedMember, title, description, langLocale, level.getLevel());
        if (attachment != null) {
            final FileConstraintChecker fcc = new FileConstraintChecker(attachment);
            if (!fcc.exists() || !fcc.isFileSmaller(3, SizeUnit.MBYTE)) {
                for (final String message : fcc.isImageAvatar()) {
                    session.notifyBad(message);
                }
                return Context.getSession().pickPreferredPage();
            }
            final FileMetadata attachmentFileMedatata = FileMetadataManager.createFromTempFile(authenticatedMember,
                                                                                                attachment,
                                                                                                attachmentFileName,
                                                                                                attachmentDescription);

            try {
                bug.addFile(attachmentFileMedatata);
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("Fail to add the attachment to the bug report."));
                throw new ShallNotPassException("Fail to add an attachment to the new bug report.", e);
            }
        }
        return new BugPageUrl(bug);
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member authenticatedMember) {
        if (attachment != null && (attachmentDescription == null || attachmentDescription.isEmpty())) {
            session.notifyError(Context.tr("You must enter a description of the attachment if you add an attachment."));
            return doProcessErrors();
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        if (milestone != null) {
            return new ReportBugPageUrl(milestone.getOffer());
        }
        return new PageNotFoundUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged in to report a bug.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getTitleParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getMilestoneParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getLangParameter());
        session.addParameter(url.getAttachmentDescriptionParameter());
    }

}
