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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bloatit.web.actions.Action;
import com.bloatit.web.actions.ContributionAction;
import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.ContributePage;
import com.bloatit.web.html.pages.CreateIdeaPage;
import com.bloatit.web.html.pages.IdeasList;
import com.bloatit.web.html.pages.GlobalSearchPage;
import com.bloatit.web.html.pages.IndexPage;
import com.bloatit.web.html.pages.LoginPage;
import com.bloatit.web.html.pages.MemberPage;
import com.bloatit.web.html.pages.MembersListPage;
import com.bloatit.web.html.pages.MyAccountPage;
import com.bloatit.web.html.pages.OfferPage;
import com.bloatit.web.html.pages.SpecialsPage;
import com.bloatit.web.html.pages.TestPage;
import com.bloatit.web.html.pages.demand.DemandPage;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.OldUrl;

public class DispatchServer {

    static final Map<String, Class<? extends Page>> pageMap;
    static final Map<String, Class<? extends Action>> actionMap;

    static {
        pageMap = new HashMap<String, Class<? extends Page>>() {

            private static final long serialVersionUID = -1990148160288171599L;

            {
                put(OldUrl.getPageName(IndexPage.class), IndexPage.class);
                put(OldUrl.getPageName(LoginPage.class), LoginPage.class);
                put(OldUrl.getPageName(IdeasList.class), IdeasList.class);
                put(OldUrl.getPageName(CreateIdeaPage.class), CreateIdeaPage.class);
                put(OldUrl.getPageName(DemandPage.class), DemandPage.class);
                put(OldUrl.getPageName(MyAccountPage.class), MyAccountPage.class);
                put(OldUrl.getPageName(SpecialsPage.class), SpecialsPage.class);
                put(OldUrl.getPageName(MembersListPage.class), MembersListPage.class);
                put(OldUrl.getPageName(MemberPage.class), MemberPage.class);
                put(OldUrl.getPageName(GlobalSearchPage.class), GlobalSearchPage.class);
                put(OldUrl.getPageName(ContributePage.class), ContributePage.class);
                put(OldUrl.getPageName(OfferPage.class), OfferPage.class);
                put(OldUrl.getPageName(TestPage.class), TestPage.class);
            }
        };

        actionMap = new HashMap<String, Class<? extends Action>>() {

            private static final long serialVersionUID = -1990148150288171599L;

            {
                put(OldUrl.getPageName(LoginAction.class), LoginAction.class);
                put(OldUrl.getPageName(LogoutAction.class), LogoutAction.class);
                put(OldUrl.getPageName(ContributionAction.class), ContributionAction.class);
                put(OldUrl.getPageName(OfferAction.class), OfferAction.class);

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

        final String linkable = query.get("page");
        final String params = query.get("param");

        final QueryString queryString = parseQueryString(params);
        final Map<String, String> parameters = mergePostGet(queryString.parameters, post, query);

        final Request request = new Request(linkable, parameters);

        try {
            if (pageMap.containsKey(linkable)) {
                Page page;
                page = pageMap.get(linkable).getConstructor(Request.class).newInstance(request);
                page.create();
                response.writePage(page);

            } else if (actionMap.containsKey(linkable)) {
                final Action action = actionMap.get(linkable).getConstructor(Request.class).newInstance(request);
                response.writeRedirect(action.process());

            } else {
            }
        } catch (final RedirectException ex) {
            response.writeRedirect(ex.getUrl());
        } catch (final InstantiationException ex) {
            Logger.getLogger(DispatchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final IllegalAccessException ex) {
            Logger.getLogger(DispatchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final IllegalArgumentException ex) {
            Logger.getLogger(DispatchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final InvocationTargetException ex) {
            Logger.getLogger(DispatchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final NoSuchMethodException ex) {
            Logger.getLogger(DispatchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final SecurityException ex) {
            Logger.getLogger(DispatchServer.class.getName()).log(Level.SEVERE, null, ex);
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
