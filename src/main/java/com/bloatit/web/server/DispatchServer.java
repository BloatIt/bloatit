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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.pages.CreateDemandPage;
import com.bloatit.web.pages.DemandPage;
import com.bloatit.web.pages.DemandsPage;
import com.bloatit.web.pages.GlobalSearchPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.pages.MembersListPage;
import com.bloatit.web.pages.MyAccountPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.SpecialsPage;

public class DispatchServer {

    static final Map<String, Class<? extends Request>> pageMap;
    static final Map<String, Class<? extends Request>> actionMap;

    static {
        pageMap = new HashMap<String, Class<? extends Request>>() {

            private static final long serialVersionUID = -1990148160288171599L;

            {
                put("index", IndexPage.class);
                put("login", LoginPage.class);
                put("demands", DemandsPage.class);
                put("demand/create", CreateDemandPage.class);
                put("demand", DemandPage.class);
                put("my_account", MyAccountPage.class);
                put("specials", SpecialsPage.class);
                put("members_list", MembersListPage.class);
                put("member", MemberPage.class);
                put("global_search", GlobalSearchPage.class);
            }
        };

        actionMap = new HashMap<String, Class<? extends Request>>() {

            private static final long serialVersionUID = -3732663492018224362L;

            {
                put("login", LoginAction.class);
                put("logout", LogoutAction.class);

            }
        };
    }
    private final Map<String, String> cookies;
    private final List<String> preferred_langs;
    private final Session session;
    private final Map<String, String> query;
    private final Map<String, String> post;

    public DispatchServer(Map<String, String> query,
                          Map<String, String> post,
                          Map<String, String> cookies,
                          List<String> preferred_langs) {
        this.cookies = cookies;
        this.preferred_langs = preferred_langs;
        session = findSession(query);

        this.query = query;
        this.post = post;

    }

    public String process() {
        com.bloatit.model.data.util.SessionManager.beginWorkUnit();

        final Request request = initCurrentRequest(query, post);

        final HtmlResult htmlResult = new HtmlResult(session, request);
        request.doProcess(htmlResult);

        final String result = htmlResult.generate();

        com.bloatit.model.data.util.SessionManager.endWorkUnitAndFlush();

        return result;
    }

    /**
     * Return the session for the user. Either an existing session or a new
     * session.
     * 
     * @param query the complet query string
     * @return the session matching the user
     */
    private Session findSession(Map<String, String> query) {
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

    private Language userLocale(Map<String, String> query) {
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

    private Request initCurrentRequest(Map<String, String> query, Map<String, String> post) {
        // Parse query string
        if (query.containsKey("page")) {
            final QueryString queryString = parseQueryString(query.get("page"));
            // Merge Post & Get
            final Map<String, String> parameters = mergePostGet(queryString.parameters, post, query);
            final Request currentRequest = findRequest(queryString.page, parameters);
            return currentRequest;
        }
        return new PageNotFound(session);
    }

    private Request findRequest(String page, Map<String, String> parameters) {
        if (page.startsWith("action/") && actionMap.containsKey(page.substring(7))) {
            // Find Action
            return RequestFactory.build(actionMap.get(page.substring(7)), session, parameters);
        }
        if (pageMap.containsKey(page)) {
            // Find page
            return RequestFactory.build(pageMap.get(page), session, parameters);
        }
        return new PageNotFound(session, parameters);
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
    private Map<String, String> mergePostGet(Map<String, String> query, Map<String, String> post, Map<String, String> get) {
        final HashMap<String, String> mergedList = new HashMap<String, String>();
        mergedList.putAll(get);
        mergedList.putAll(query);
        mergedList.putAll(post);

        return mergedList;
    }

    private QueryString parseQueryString(String queryString) {
        final String[] splitted = strip(queryString, '/').split("/");
        String page = "";
        final Map<String, String> parameters = new HashMap<String, String>();

        int i = 0;
        // Parsing, finding
        while (i < splitted.length && !splitted[i].contains("-")) {
            if (!page.isEmpty() && !splitted[i].isEmpty()) {
                page = page + "/";
            }
            page = page + splitted[i];
            i = i + 1;
        }

        // Parsing, finding page parameters
        while (i < splitted.length) {
            if (splitted[i].contains("-")) {
                final String[] p = splitted[i].split("-");
                parameters.put(p[0], p[1]);
            }
            i = i + 1;
        }
        return new QueryString(page, parameters);
    }

    private static class QueryString {

        public String page;
        public Map<String, String> parameters;

        private QueryString(String page, Map<String, String> parameters) {
            this.page = page;
            this.parameters = parameters;
        }
    }

    private static String strip(String string, char stripped) {
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
