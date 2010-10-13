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

import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.pages.DemandPage;
import com.bloatit.web.pages.DemandsPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MyAccountPage;
import com.bloatit.web.pages.PageNotFound;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DispatchServer {

    static final Map<String, RequestFactory> pageMap;
    static final Map<String, RequestFactory> actionMap;

    static {
        pageMap = new HashMap<String, RequestFactory>() {
            {
                put("index", new RequestFactory<IndexPage>(IndexPage.class));
                put("login", new RequestFactory<LoginPage>(LoginPage.class));
                put("demands", new RequestFactory<DemandsPage>(DemandsPage.class));
                put("demand", new RequestFactory<DemandPage>(DemandPage.class));
                put("my_account", new RequestFactory<MyAccountPage>(MyAccountPage.class));
            }
    
        };

        actionMap = new HashMap<String, RequestFactory>() {
            {
                put("login", new RequestFactory<LoginAction>(LoginAction.class));
                put("logout", new RequestFactory<LogoutAction>(LogoutAction.class));
               
            }

        };
    }




    private Map<String, String> query;
    private Map<String, String> post;
    private Map<String, String> cookies;
    private List<String> preferred_langs;
    private Session session;

    public DispatchServer(Map<String, String> query, Map<String, String> post, Map<String, String> cookies, List<String> preferred_langs) {
        this.query = query;
        this.post = post;
        this.cookies = cookies;
        this.preferred_langs = preferred_langs;
    }

    public String process() throws NoSuchAlgorithmException {
        initSession();
        initLanguage();

        //TODO : Combine query, post & cookies in a single map

        Request currentRequest = initCurrentRequest();

        HtmlResult htmlResult = new HtmlResult(session);

        currentRequest.doProcess(htmlResult, query, post);

        return htmlResult.generate();
    }

    private void initSession() throws NoSuchAlgorithmException {
        session = null;
        if(cookies.containsKey("session_key")) {
            session = SessionManager.getByKey(cookies.get("session_key"));
        }

        if(session == null) {
            session = SessionManager.createSession();
        }
    }

    private void initLanguage() {
        Language language = new Language();
        if(query.containsKey("lang")) {
            if(query.get("lang").equals("default")) {
                language.findPrefered(preferred_langs);
            } else {
                language.setCode(query.get("lang"));
            }
        }

        session.setLanguage(language);
    }

    private Request initCurrentRequest() {

        Request request = findAction();

        if(request == null) {
            request = findPage();
        }

        return request;
    }

    private Request findAction() {

        if(query.containsKey("page")) {
            QueryString queryString = parseQueryString(query.get("page"));
            if(queryString.page.startsWith("action/") && actionMap.containsKey(queryString.page.substring(7))) {
                return actionMap.get(queryString.page.substring(7)).build(session, queryString.parameters);
            }
        }
        return null;
    }

    private Request findPage() {

        if(query.containsKey("page")) {
            QueryString queryString = parseQueryString(query.get("page"));
            if(pageMap.containsKey(queryString.page)) {
                return pageMap.get(queryString.page).build(session, queryString.parameters);
            }
        }

        return new RequestFactory<PageNotFound>(PageNotFound.class).build(session);
    }

    private QueryString parseQueryString(String queryString) {
        String[] splitted = strip(queryString, '/').split("/");

        String page = "";
        Map<String, String> parameters = new HashMap<String, String>();

        int i = 0;

         
        
        // Parsing, finding        page name  page name
        while( i < splitted.length && !splitted[i].contains("-")) {
            if(!page.isEmpty() && !splitted[i].isEmpty()) {
                page = page + "/";
            }
            page = page + splitted[i];
            i = i+1;
        }

        // Parsing, finding page parameters
        while (i < splitted.length){
            if (splitted[i].contains("-")) {
                String[] p = splitted[i].split("-");
                parameters.put(p[0],p[1]);
            }
            i = i+1;
        }

        return new QueryString(page, parameters);

    }

    private static class QueryString {
        public String page;
        public Map<String, String> parameters;

        public QueryString() {

        }

        private QueryString(String page, Map<String, String> parameters) {
            this.page = page;
            this.parameters = parameters;
        }
    }

    private static String strip(String string, char stripped) {
        String result1 = "";

        int i = 0;

        while(string.charAt(i) == stripped) {
            i++;
        }

        for (; i < string.length(); i++) {
            result1 += string.charAt(i);
        }

        i =  result1.length() -1;


        String result2 = "";

        while(result1.charAt(i) == stripped) {
            i--;
        }

        for (; i >= 0; i--) {
            result2 = result1.charAt(i) + result2;
        }
        

        return result2;
    }


}
