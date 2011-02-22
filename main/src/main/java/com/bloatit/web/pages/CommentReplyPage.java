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
package com.bloatit.web.pages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Comment;
import com.bloatit.web.actions.CommentCommentAction;
import com.bloatit.web.url.CommentCommentActionUrl;
import com.bloatit.web.url.CommentReplyPageUrl;

/**
 * Page that hosts the form used to reply to an existing comment
 */
@ParamContainer("comment/reply")
public final class CommentReplyPage extends LoggedPage {

    private static final int NB_LINES = 10;
    private static final int NB_COLUMNS = 80;

    private final CommentReplyPageUrl url;

    @RequestParam(name = CommentCommentAction.COMMENT_TARGET)
    private final Comment targetComment;

    public CommentReplyPage(final CommentReplyPageUrl url) {
        super(url);
        this.url = url;
        this.targetComment = url.getTargetComment();
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        session.notifyList(url.getMessages());

        final HtmlTitleBlock htb = new HtmlTitleBlock(Context.tr("Reply to a comment"), 1);

        final CommentCommentActionUrl commentCommentActionUrl = new CommentCommentActionUrl(targetComment);
        final HtmlForm form = new HtmlForm(commentCommentActionUrl.urlString());
        htb.add(form);

        final FormFieldData<String> createFormFieldData = commentCommentActionUrl.getCommentParameter().formFieldData();
        final HtmlTextArea commentInput = new HtmlTextArea(createFormFieldData, Context.tr("Content"), NB_LINES, NB_COLUMNS);
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
    protected String getPageTitle() {
        return Context.tr("Reply to a comment");
    }

    @Override
    public boolean isStable() {
        return false;
    }

}
