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

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Comment;
import com.bloatit.model.Commentable;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.url.CreateCommentActionUrl;

/**
 * A response to a form used to create a comment to a content
 */
@ParamContainer("comment/docomment")
public final class CreateCommentAction extends LoggedAction {
    @ParamConstraint(optionalErrorMsg = @tr("The comment must be post on a commentable thing"))
    @RequestParam(name = "target")
    private final Commentable commentable;

    @ParamConstraint(optionalErrorMsg = @tr("You must type a comment"),//
    /*            */ min = "2", minErrorMsg = @tr("The comment must be at least 2 characters long."))
    @RequestParam(role = Role.POST)
    private final String comment;

    @Optional
    @RequestParam(name = "attachment", role = Role.POST)
    private final String attachment;

    @Optional
    @RequestParam(name = "attachment/filename", role = Role.POST)
    private final String attachmentFileName;

    @Optional
    @RequestParam(name = "attachment_description", role = Role.POST)
    private final String attachmentDescription;

    @SuppressWarnings("unused")
    @Optional
    @RequestParam(name = "attachment/contenttype", role = Role.POST)
    private final String attachmentContentType;

    private final CreateCommentActionUrl url;

    public CreateCommentAction(final CreateCommentActionUrl url) {
        super(url);
        this.url = url;
        this.commentable = url.getCommentable();
        this.comment = url.getComment();
        this.attachment = url.getAttachment();
        this.attachmentFileName = url.getAttachmentFileName();
        this.attachmentContentType = url.getAttachmentContentType();
        this.attachmentDescription = url.getAttachmentDescription();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member authenticatedMember) {
        // add a can access comment.
        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member authenticatedMember) {
        try {
            final Comment newComment = commentable.addComment(comment);

            if (attachment != null && (attachmentDescription == null || attachmentDescription.isEmpty())) {
                session.notifyError(Context.tr("You must enter a description of the attachment if you add an attachment."));
                return redirectWithError();
            }

            if (attachment != null) {
                final FileMetadata attachmentFileMedatata = FileMetadataManager.createFromTempFile(authenticatedMember,
                                                                                                    attachment,
                                                                                                    attachmentFileName,
                                                                                                    attachmentDescription);

                newComment.addFile(attachmentFileMedatata);
            }

            session.notifyGood(Context.tr("Your comment has been added."));
        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("Error commenting. Please notify us"));
            throw new ShallNotPassException("user couldn't create to a comment", e);
        }

        return session.pickPreferredPage();
    }

    public Url redirectWithError() {
        session.addParameter(url.getCommentParameter());
        session.addParameter(url.getCommentableParameter());
        session.addParameter(url.getAttachmentDescriptionParameter());
        return Context.getSession().getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        return redirectWithError();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged in to comment.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getCommentParameter());
    }
}
