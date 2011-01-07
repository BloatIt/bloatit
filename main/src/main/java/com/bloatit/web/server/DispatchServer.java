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
import java.util.List;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.web.actions.Action;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.PageNotFound;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.i18n.Localizator;
import com.bloatit.web.utils.url.AccountChargingActionUrl;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;
import com.bloatit.web.utils.url.CreateIdeaActionUrl;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;
import com.bloatit.web.utils.url.GlobalSearchPageUrl;
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

				put(LoginActionUrl.getName(), LoginActionUrl.class);
				put(LogoutActionUrl.getName(), LogoutActionUrl.class);
				put(ContributionActionUrl.getName(), ContributionActionUrl.class);
				put(OfferActionUrl.getName(), OfferActionUrl.class);
				put(AccountChargingActionUrl.getName(), AccountChargingActionUrl.class);
				put(CreateIdeaActionUrl.getName(), CreateIdeaActionUrl.class);
				put(RegisterActionUrl.getName(), RegisterActionUrl.class);
				put(KudoActionUrl.getName(), KudoActionUrl.class);
			}
		};
	}
	private final Map<String, String> cookies;
	private final List<String> preferredLangs;
	private final Session session;
	private final Map<String, String> query;
	private final Map<String, String> post;


	public DispatchServer(final Map<String, String> query, final Map<String, String> post, final Map<String, String> cookies,
	        final List<String> preferredLangs) {
		this.cookies = cookies;
		this.preferredLangs = preferredLangs;

		this.session = findSession(query);

		this.query = query;
		this.post = post;
	}

	/**
	 * Creates a localizator
	 */
	private Localizator generateLocalizator() {
		return new Localizator(query.get("lang"), preferredLangs);
    }

	public void process(final HttpResponse response) throws IOException {
		com.bloatit.model.data.util.SessionManager.beginWorkUnit();

		Context.setSession(session);
		Context.setLocalizator(generateLocalizator());

		final String pageCode = query.get("page");
		final String params = query.get("param");

		// Add the get params
		final Parameters parameters = parseQueryString(params);
		// Merge with the query params
		parameters.putAll(query);
		// Merge with the post paramsLocale
		parameters.putAll(post);

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
	 * @param query the complete query string
	 * @return the session matching the user
	 */
	private Session findSession(final Map<String, String> query) {
		Session sess = null;

		if (cookies.containsKey("session_key")) {
			sess = SessionManager.getByKey(cookies.get("session_key"));
		}
		if (sess == null) {
			sess = SessionManager.createSession();
		}
		return sess;
	}

	private Parameters parseQueryString(final String queryString) {
		final Parameters parameters = new Parameters();

		if (queryString != null) {
			final String[] splitted = strip(queryString, '/').split("/");

			int i = 0;

			// Parsing, finding page parameters
			while (i < splitted.length) {
				if (splitted[i].contains("-")) {
					final String[] p = splitted[i].split("-", 2);
					parameters.put(p[0], p[1]);
				}
				i = i + 1;
			}
		}
		return parameters;
	}

	private static String strip(final String string, final char stripped) {
		int begin = 0;
		while (string.charAt(begin) == stripped) {
			begin++;
		}
		int end = string.length() - 1;
		while (string.charAt(end) == stripped) {
			end--;
		}
		return string.substring(begin, end + 1);
	}
}
