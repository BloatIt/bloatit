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

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Commentable;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.LoginPageUrl;

/**
 * A response to a form used to create a comment to a content
 */
@ParamContainer("comment/docomment")
public final class CreateCommentAction extends LoggedAction {
    public static final String COMMENT_CONTENT_CODE = "bloatit_comment_content";
    public static final String COMMENT_TARGET = "target";

    @RequestParam(name = COMMENT_TARGET)
    private final Commentable commentable;

    @RequestParam(name = COMMENT_CONTENT_CODE, role = Role.POST)
    private final String comment;

    private final CreateCommentActionUrl url;

    public CreateCommentAction(final CreateCommentActionUrl url) {
        super(url);
        this.url = url;
        this.commentable = url.getCommentable();
        this.comment = url.getComment();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        session.notifyList(url.getMessages());
        session.notifyGood(Context.tr("Your comment has been added."));
        try {
            commentable.addComment(comment);
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to add a comment on this idea."));
            return session.pickPreferredPage();
        }

        return session.pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());
        return new LoginPageUrl();
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
