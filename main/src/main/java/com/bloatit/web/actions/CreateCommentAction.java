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

import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.url.Url;
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
    public static final String COMMENT_CONTENT_CODE = "bloatit_comment_content";
    public static final String COMMENT_TARGET = "target";
    public static final String ATTACHEMENT_CODE = "attachement";
    public static final String ATTACHEMENT_NAME_CODE = "attachement/filename";
    public static final String ATTACHEMENT_CONTENT_TYPE_CODE = "attachement/contenttype";
    public static final String ATTACHEMENT_DESCRIPTION_CODE = "attachement_description";

    @ParamConstraint(optionalErrorMsg = @tr("The comment must be post on a commentable thing"))
    @RequestParam(name = COMMENT_TARGET)
    private final Commentable commentable;

    @ParamConstraint(optionalErrorMsg = @tr("You must type a comment") )
    @RequestParam(name = COMMENT_CONTENT_CODE, role = Role.POST)
    private final String comment;

    @Optional
    @RequestParam(name = ATTACHEMENT_CODE, role = Role.POST)
    private final String attachement;

    @Optional
    @RequestParam(name = ATTACHEMENT_NAME_CODE, role = Role.POST)
    private final String attachementFileName;

    @Optional
    @RequestParam(name = ATTACHEMENT_DESCRIPTION_CODE, role = Role.POST)
    private final String attachementDescription;

    @Optional
    @RequestParam(name = ATTACHEMENT_CONTENT_TYPE_CODE, role = Role.POST)
    private final String attachementContentType;

    private final CreateCommentActionUrl url;

    public CreateCommentAction(final CreateCommentActionUrl url) {
        super(url);
        this.url = url;
        this.commentable = url.getCommentable();
        this.comment = url.getComment();
        this.attachement = url.getAttachement();
        this.attachementFileName = url.getAttachementFileName();
        this.attachementContentType = url.getAttachementContentType();
        this.attachementDescription = url.getAttachementDescription();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        session.notifyList(url.getMessages());
        try {
            Comment newComment = commentable.addComment(comment);

            if (attachement != null && (attachementDescription == null || attachementDescription.isEmpty())) {
                session.notifyError(Context.tr("You must enter a description of the attachement if you add an attachement."));
                return redirectWithError();
            }

            if (attachement != null) {
                final FileMetadata attachementFileMedatata = FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(),
                                                                                                    attachement,
                                                                                                    attachementFileName,
                                                                                                    attachementDescription);

                newComment.addFile(attachementFileMedatata);
            }

            session.notifyGood(Context.tr("Your comment has been added."));
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to add a comment on this that."));
            return session.pickPreferredPage();
        }

        return session.pickPreferredPage();
    }

    public Url redirectWithError() {
        session.addParameter(url.getCommentParameter());
        session.addParameter(url.getCommentableParameter());
        session.addParameter(url.getAttachementDescriptionParameter());
        return Context.getSession().getLastVisitedPage();
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
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
