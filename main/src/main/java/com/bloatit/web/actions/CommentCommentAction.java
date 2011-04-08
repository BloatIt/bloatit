/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.model.Comment;
import com.bloatit.model.Member;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.LoginPageUrl;

/**
 * A response to a form used to create a response to a comment
 */
@ParamContainer("comment/docomment")
public class CommentCommentAction extends LoggedAction {
    public static final String COMMENT_CONTENT_CODE = "bloatit_comment_content";
    public static final String COMMENT_TARGET = "target";

    @RequestParam(name = COMMENT_TARGET)
    private final Comment targetComment;

    @RequestParam(name = COMMENT_CONTENT_CODE, role = Role.POST)
    private final String comment;

    private final CommentCommentActionUrl url;

    public CommentCommentAction(final CommentCommentActionUrl url) {
        super(url);
        this.url = url;
        this.targetComment = url.getTargetComment();
        this.comment = url.getComment();
    }

    @Override
    public final Url doProcessRestricted(Member authenticatedMember) {
        session.notifyList(url.getMessages());
        session.notifyGood(Context.tr("Your comment has been added."));

        try {
            targetComment.addComment(comment);
        } catch (final UnauthorizedOperationException e) {
            // TODO do some stuff here.
        }

        return session.pickPreferredPage();
    }

    @Override
    protected final Url doProcessErrors() {
        session.notifyList(url.getMessages());
        return new LoginPageUrl();
    }

    @Override
    protected final String getRefusalReason() {
        return Context.tr("You must be logged in to comment.");
    }

    @Override
    protected final void transmitParameters() {
        session.addParameter(url.getCommentParameter());
    }
}
