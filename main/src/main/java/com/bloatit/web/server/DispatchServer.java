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
package com.bloatit.web.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloatit.web.actions.Action;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.PageNotFound;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.AccountChargingActionUrl;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;
import com.bloatit.web.utils.url.GlobalSearchPageUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.IdeasListUrl;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.LoginActionUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.LogoutActionUrl;
import com.bloatit.web.utils.url.MemberPageUrl;
import com.bloatit.web.utils.url.MembersListPageUrl;
import com.bloatit.web.utils.url.MyAccountPageUrl;
import com.bloatit.web.utils.url.OfferActionUrl;
import com.bloatit.web.utils.url.OfferPageUrl;
import com.bloatit.web.utils.url.Parameters;
import com.bloatit.web.utils.url.SpecialsPageUrl;
import com.bloatit.web.utils.url.TestPageUrl;
import com.bloatit.web.utils.url.Url;

public class DispatchServer {

    static final Map<String, Class<? extends Url>> urlMap;

    static {
        urlMap = new HashMap<String, Class<? extends Url>>() {

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

                put(LoginActionUrl.getName(), LoginActionUrl.class);
                put(LogoutActionUrl.getName(), LogoutActionUrl.class);
                put(ContributionActionUrl.getName(), ContributionActionUrl.class);
                put(OfferActionUrl.getName(), OfferActionUrl.class);
                put(AccountChargingActionUrl.getName(), AccountChargingActionUrl.class);

            }
        };
    }
    private final Map<String, String> cookies;
    private final List<String> preferred_langs;
    private final Session session;
    private final Map<String, String> query;
    private final Map<String, String> post;

    public DispatchServer(final Map<String, String> query,
                          final Map<String, String> post,
                          final Map<String, String> cookies,
                          final List<String> preferred_langs) {
        this.cookies = cookies;

        this.preferred_langs = preferred_langs;
        session = findSession(query);
        Context.setSession(session);

        this.query = query;
        this.post = post;
    }

    public void process(final HttpResponse response) throws IOException {
        com.bloatit.model.data.util.SessionManager.beginWorkUnit();

        final String pageCode = query.get("page");
        final String params = query.get("param");

        final QueryString queryString = parseQueryString(params);

        // Should be merge post/get/session
        final Parameters parameters = new Parameters();
        parameters.putAll(mergePostGet(queryString.parameters, post, query));

        try {
            if (urlMap.containsKey(pageCode)) {
                Url aUrl;
                aUrl = urlMap.get(pageCode).getConstructor(Parameters.class).newInstance(parameters);
                Linkable linkable = aUrl.createPage();

                if (linkable instanceof Page) {
                    Page page = Page.class.cast(linkable);
                    page.create();
                    response.writePage(page);
                } else if (linkable instanceof Action) {
                    Action action = Action.class.cast(linkable);
                    response.writeRedirect(action.process());
                }
            } else {
                session.notifyError(session.tr("Unknow page code: ") + pageCode);
                final Page page = new PageNotFound(null);
                page.create();
                response.writePage(page);
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RedirectException e) {
            response.writeRedirect(e.getUrl());
        }

        com.bloatit.model.data.util.SessionManager.endWorkUnitAndFlush();

    }

    /**
     * Return the session for the user. Either an existing session or a new
     * session.
     * 
     * @param query the complet query string
     * @return the session matching the user
     */
    private Session findSession(final Map<String, String> query) {
        Session sess = null;
        final Language l = userLocale(query);

        if (cookies.containsKey("session_key")) {
            sess = SessionManager.getByKey(cookies.get("session_key"));

        }
        if (sess == null) {
            sess = SessionManager.createSession();

        }
        sess.setLanguage(l);

        return sess;

    }

    private Language userLocale(final Map<String, String> query) {
        final Language language = new Language();

        if (query.containsKey("lang")) {
            if (query.get("lang").equals("default")) {
                language.findPrefered(preferred_langs);

            } else {
                language.setCode(query.get("lang"));

            }
        }

        return language;

    }

    /**
     * Merges the list of query attributes and the list of post attributes.
     * 
     * If an attribute with the same key is found in Query and Post, the
     * attribute
     * from post is kept.
     * 
     * @param query the Map containing the query parameters
     * @param post the Map containing the post parameters
     * @return the new map
     */
    private Map<String, String> mergePostGet(final Map<String, String> query, final Map<String, String> post, final Map<String, String> get) {
        final HashMap<String, String> mergedList = new HashMap<String, String>();
        mergedList.putAll(get);
        mergedList.putAll(query);
        mergedList.putAll(post);

        return mergedList;

    }

    private QueryString parseQueryString(final String queryString) {
        final Map<String, String> parameters = new HashMap<String, String>();
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
        return new QueryString(parameters);

    }

    private static class QueryString {

        public Map<String, String> parameters;

        private QueryString(final Map<String, String> parameters) {
            this.parameters = parameters;
        }
    }

    private static String strip(final String string, final char stripped) {
        String result1 = "";
        int i = 0;
        
        while (string.charAt(i) == stripped) {
            i++;
        }
        for (; i < string.length(); i++) {
            result1 += string.charAt(i);
        }
        i = result1.length() - 1;

        String result2 = "";
        while (result1.charAt(i) == stripped) {
            i--;
        }
        for (; i >= 0; i--) {
            result2 = result1.charAt(i) + result2;
        }
        return result2;
    }
}
