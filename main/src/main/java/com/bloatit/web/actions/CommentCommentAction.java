/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import com.bloatit.framework.Comment;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.CommentCommentActionUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer("comment/docomment")
public class CommentCommentAction extends LoggedAction {
	public static final String COMMENT_CONTENT_CODE = "bloatit_comment_content";
	public static final String COMMENT_TARGET = "target";

	@RequestParam(name = COMMENT_TARGET, level = Level.ERROR)
	private final Comment targetComment;

	@RequestParam(name = COMMENT_CONTENT_CODE, role = Role.POST, level = Level.ERROR)
	private final String comment;

	private CommentCommentActionUrl url;

	public CommentCommentAction(final CommentCommentActionUrl url) throws RedirectException {
		super(url);
		this.url = url;
		this.targetComment = url.getTargetComment();
		this.comment = url.getComment();
	}

	@Override
	public final Url doProcessRestricted() throws RedirectException {
		session.notifyList(url.getMessages());
		session.notifyGood(Context.tr("Your comment has been added."));
		
		targetComment.authenticate(session.getAuthToken());
		targetComment.addChildComment(comment);

		return session.pickPreferredPage();
	}

	@Override
	protected final Url doProcessErrors() throws RedirectException {
		session.notifyList(url.getMessages());
		return new LoginPageUrl();
	}

	@Override
	protected final String getRefusalReason() {
		return Context.tr("You must be logged in to comment.");
	}

	@Override
	protected final void transmitParameters() {
		session.addParam(COMMENT_CONTENT_CODE, comment);
	}
}
