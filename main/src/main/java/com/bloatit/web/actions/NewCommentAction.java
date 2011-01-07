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

import com.bloatit.framework.Demand;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.NewCommentActionUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer("docomment")
public class NewCommentAction extends Action {
	public static final String COMMENT_CONTENT_CODE = "bloatit_comment_content";
	public static final String COMMENT_TARGET = "target";
	
	
	@RequestParam(name = COMMENT_TARGET, level=Level.ERROR)
	private Demand targetIdea;
	
	@RequestParam(name = COMMENT_CONTENT_CODE, role = Role.POST, level=Level.ERROR)
	private String comment;

	
	private NewCommentActionUrl url;

	public NewCommentAction(final NewCommentActionUrl url) throws RedirectException {
		super(url);
		this.url = url;
		this.targetIdea = url.getTargetIdea();
		this.comment = url.getComment();
	}

	@Override
	protected final Url doProcess() throws RedirectException {
		session.notifyList(url.getMessages());
		
		targetIdea.authenticate(session.getAuthToken());
		targetIdea.addComment(comment);
		
		return session.pickPreferredPage();
	}
	
    @Override
	protected Url doProcessErrors() throws RedirectException {
    	//TODO
		return new LoginPageUrl();
	}
}
