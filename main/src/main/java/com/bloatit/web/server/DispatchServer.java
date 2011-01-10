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
package com.bloatit.web.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.web.actions.Action;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.PageNotFound;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.scgiserver.HttpPost;
import com.bloatit.web.utils.i18n.Localizator;
import com.bloatit.web.utils.url.AccountChargingActionUrl;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.CommentCommentActionUrl;
import com.bloatit.web.utils.url.CommentReplyPageUrl;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;
import com.bloatit.web.utils.url.CreateIdeaActionUrl;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;
import com.bloatit.web.utils.url.GlobalSearchPageUrl;
import com.bloatit.web.utils.url.IdeaCommentActionUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.IdeasListUrl;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.KudoActionUrl;
import com.bloatit.web.utils.url.LoginActionUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.LogoutActionUrl;
import com.bloatit.web.utils.url.MemberPageUrl;
import com.bloatit.web.utils.url.MembersListPageUrl;
import com.bloatit.web.utils.url.MyAccountPageUrl;
import com.bloatit.web.utils.url.OfferActionUrl;
import com.bloatit.web.utils.url.OfferPageUrl;
import com.bloatit.web.utils.url.Parameters;
import com.bloatit.web.utils.url.PaylineActionUrl;
import com.bloatit.web.utils.url.PaylineNotifyActionUrl;
import com.bloatit.web.utils.url.PaylinePageUrl;
import com.bloatit.web.utils.url.RegisterActionUrl;
import com.bloatit.web.utils.url.RegisterPageUrl;
import com.bloatit.web.utils.url.SpecialsPageUrl;
import com.bloatit.web.utils.url.TestPageUrl;
import com.bloatit.web.utils.url.Url;

public class DispatchServer {
	static final Map<String, Class<? extends Url>> URL_MAP;

	static {
		URL_MAP = new HashMap<String, Class<? extends Url>>() {
			private static final long serialVersionUID = -1990148160288171599L;
			{
				put(IndexPageUrl.getName(), IndexPageUrl.class);
				put(LoginPageUrl.getName(), LoginPageUrl.class);
				put(IdeasListUrl.getName(), IdeasListUrl.class);
				put(CreateIdeaPageUrl.getName(), CreateIdeaPageUrl.class);
				put(IdeaPageUrl.getName(), IdeaPageUrl.class);
				put(MyAccountPageUrl.getName(), MyAccountPageUrl.class);
				put(SpecialsPageUrl.getName(), SpecialsPageUrl.class);
				put(MembersListPageUrl.getName(), MembersListPageUrl.class);
				put(MemberPageUrl.getName(), MemberPageUrl.class);
				put(GlobalSearchPageUrl.getName(), GlobalSearchPageUrl.class);
				put(ContributePageUrl.getName(), ContributePageUrl.class);
				put(OfferPageUrl.getName(), OfferPageUrl.class);
				put(TestPageUrl.getName(), TestPageUrl.class);
				put(AccountChargingPageUrl.getName(), AccountChargingPageUrl.class);
				put(RegisterPageUrl.getName(), RegisterPageUrl.class);
				put(PaylinePageUrl.getName(), PaylinePageUrl.class);
				put(CommentReplyPageUrl.getName(), CommentReplyPageUrl.class);

				put(LoginActionUrl.getName(), LoginActionUrl.class);
				put(LogoutActionUrl.getName(), LogoutActionUrl.class);
				put(ContributionActionUrl.getName(), ContributionActionUrl.class);
				put(OfferActionUrl.getName(), OfferActionUrl.class);
				put(AccountChargingActionUrl.getName(), AccountChargingActionUrl.class);
				put(CreateIdeaActionUrl.getName(), CreateIdeaActionUrl.class);
				put(RegisterActionUrl.getName(), RegisterActionUrl.class);
				put(KudoActionUrl.getName(), KudoActionUrl.class);
				put(IdeaCommentActionUrl.getName(), IdeaCommentActionUrl.class);
				put(PaylineActionUrl.getName(), PaylineActionUrl.class);
				put(PaylineNotifyActionUrl.getName(), PaylineNotifyActionUrl.class);
				put(IdeaCommentActionUrl.getName(), IdeaCommentActionUrl.class);
                put(CommentCommentActionUrl.getName(), CommentCommentActionUrl.class);
			}
		};
	}

	private final Session session;
	private final HttpHeader header;
	private final HttpPost post;


	public DispatchServer(HttpHeader header, HttpPost post) {
		this.header = header;
        this.post = post;
		this.session = findSession();
	}

	/**
	 * Creates a localizator
	 */
	private Localizator generateLocalizator() {
		return new Localizator(header.getQueryString().getLanguage(), header.getHttpAcceptLanguage());
    }

	public void process(final HttpResponse response) throws IOException {
		com.bloatit.model.data.util.SessionManager.beginWorkUnit();

		Context.setSession(session);
		Context.setLocalizator(generateLocalizator());

		final String pageCode = header.getQueryString().getPageName();


		// Merge post and get parameters.
		final Parameters parameters = new Parameters();
		parameters.putAll(header.getQueryString().getParameters());
		parameters.putAll(post.getParameters());

		try {
			Url url = constructUrl(pageCode, parameters);
			if (url != null) {
				final Linkable linkable = url.createPage();

				// If its a page then create a page
				if (linkable instanceof Page) {
					final Page page = Page.class.cast(linkable);
					page.create();
					response.writePage(page);
					// If its an action then create an action
				} else if (linkable instanceof Action) {
					final Action action = Action.class.cast(linkable);
					response.writeRedirect(action.process().urlString());
				}
			} else {
				session.notifyError(Context.tr("Unknow page: ") + pageCode);
				final Page page = new PageNotFound(null);
				page.create();
				response.writePage(page);
			}
		} catch (final RedirectException e) {
			Log.web().info("Redirect to " + e.getUrl(), e);
			response.writeRedirect(e.getUrl().urlString());
		}
		com.bloatit.model.data.util.SessionManager.endWorkUnitAndFlush();
	}

	@SuppressWarnings("deprecation") // It's OK
    private Url constructUrl(String pageCode, Parameters params) {
		try {
			Class<? extends Url> urlClass = URL_MAP.get(pageCode);
			if (urlClass != null) {
				return urlClass.getConstructor(Parameters.class, Parameters.class).newInstance(params, session.getParams());
			}
		} catch (final IllegalArgumentException e) {
			Log.web().error("IllegalArgument calling url constructor.", e);
		} catch (final SecurityException e) {
			Log.web().error("SecurityException calling url constructor.", e);
		} catch (final InstantiationException e) {
			Log.web().error("InstantiationException calling url constructor.", e);
		} catch (final IllegalAccessException e) {
			Log.web().error("IllegalAccessException calling url constructor.", e);
		} catch (final InvocationTargetException e) {
			Log.web().error("InvocationTargetException calling url constructor.", e);
		} catch (final NoSuchMethodException e) {
			Log.web().error("NoSuchMethodException calling url constructor.", e);
		}
		return null;
	}

	/**
	 * Return the session for the user. Either an existing session or a new session.
	 * @return the session matching the user
	 */
	private Session findSession() {
		String key = header.getHttpCookie().get("session_key");
		Session sessionByKey = null;
        if (key != null && (sessionByKey = SessionManager.getByKey(key)) != null) {
		    return sessionByKey;
		}
        return  SessionManager.createSession();
	}

}
