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

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.FileConstraintChecker.SizeUnit;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Comment;
import com.bloatit.model.Commentable;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.usercontent.CommentForm;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.CreateCommentActionUrl;

/**
 * A response to a form used to create a comment to a content
 */
@ParamContainer("comment/docomment")
public final class CreateCommentAction extends UserContentAction {

    @NonOptional(@tr("The comment must be posted on a commentable thing"))
    @RequestParam(name = "target")
    private final Commentable commentable;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must type a comment"))
    @MinConstraint(min = 2, message = @tr("The comment must be at least 2 characters long."))
    private final String comment;

    private final CreateCommentActionUrl url;

    public CreateCommentAction(final CreateCommentActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;
        this.commentable = url.getCommentable();
        this.comment = url.getComment();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        // add a can access comment.
        return NO_ERROR;
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        try {
            final Comment newComment = commentable.addComment(comment);
            propagateAttachedFileIfPossible(newComment);
            session.notifyGood(Context.tr("Your comment has been added."));
        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("Error commenting. Please notify us"));
            throw new ShallNotPassException("user couldn't create to a comment", e);
        }
        return session.pickPreferredPage();
    }

    private Url redirectWithError() {
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
    protected void doTransmitParameters() {
        session.addParameter(url.getCommentParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        return new FileConstraintChecker(filename).isFileSmaller(CommentForm.FILE_MAX_SIZE_MIO, SizeUnit.MBYTE);
    }
}
