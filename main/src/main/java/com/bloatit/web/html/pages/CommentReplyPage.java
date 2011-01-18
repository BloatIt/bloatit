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
package com.bloatit.web.html.pages;

import com.bloatit.framework.Comment;
import com.bloatit.web.actions.CommentCommentAction;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.CommentCommentActionUrl;
import com.bloatit.web.utils.url.CommentReplyPageUrl;

/**
 * Page that hosts the form used to reply to an existing comment
 */
@ParamContainer("comment/reply")
public final class CommentReplyPage extends LoggedPage {

    private static final int NB_LINES = 10;
    private static final int NB_COLUMNS = 80;

    private final CommentReplyPageUrl url;

    @RequestParam(name = CommentCommentAction.COMMENT_TARGET, level = Level.ERROR)
    private final Comment targetComment;

    @RequestParam(name = CommentCommentAction.COMMENT_CONTENT_CODE, role = Role.SESSION, defaultValue = "")
    private final String comment;

    public CommentReplyPage(final CommentReplyPageUrl url) {
        super(url);
        this.url = url;
        this.targetComment = url.getTargetComment();
        this.comment = url.getComment();
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        session.notifyList(url.getMessages());

        final HtmlTitleBlock htb = new HtmlTitleBlock(Context.tr("Reply to a comment"), 1);

        final HtmlForm form = new HtmlForm(new CommentCommentActionUrl(targetComment).urlString());
        htb.add(form);

        final HtmlTextArea commentInput = new HtmlTextArea(CommentCommentAction.COMMENT_CONTENT_CODE, Context.tr("Content"), NB_LINES, NB_COLUMNS);
        commentInput.setDefaultValue(comment);
        form.add(commentInput);

        final HtmlSubmit submit = new HtmlSubmit(Context.tr("Submit"));
        form.add(submit);

        return htb;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("you must be logged in to comment.");
    }

    @Override
    protected String getTitle() {
        return Context.tr("Reply to a comment");
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
