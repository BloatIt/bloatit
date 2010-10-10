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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DispatchServer {

    static final Map<String, PageFactory> pageMap;
    static final Map<String, ActionFactory> actionMap;

    static {
        pageMap = new HashMap<String, PageFactory>() {
            {
                put("index", new PageFactory<IndexPage>());
                put("login", new PageFactory<LoginPage>());
                put("demands", new PageFactory<DemandsPage>());
                put("demand", new PageFactory<DemandPage>());
                put("my_account", new PageFactory<MyAccountPage>());
            }
    
        };

        actionMap = new HashMap<String, ActionFactory>() {
            {
                put("login", new ActionFactory<LoginAction>());
                put("logout", new ActionFactory<LogoutAction>());
               
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

    public String process() {
        initSession();
        initLanguage();

        //TODO : Combine query, post & cookies in a single map

        Request currentRequest = initCurrentRequest();

        HtmlResult htmlResult = new HtmlResult(session);

        currentRequest.doProcess(htmlResult, query, post);

        return htmlResult.generate();
    }

    
        

    /*def find_action(self):
        if "page" in self.query:
            page,parameters = self._parse_query_string(self.query['page'][0])
            if page.startswith("action/") and page[7:] in self.action_map:
                return self.action_map[page[7:]](self.session, parameters=parameters)
        return None

    def find_page(self):
        if "page" in self.query:
            page,parameters = self._parse_query_string(self.query['page'][0])
            if page in self.page_map:
                return self.page_map[page](self.session, parameters=parameters)

        return PageNotFoundContent(self.session)

    def _parse_query_string(self, query):
        """
Parse the query string to find page name and all parameters

Parameter format must be :
/name-value
will return a tuple with page name first and a map with all parameters
second. Parameter map is formatted {name : value, ... }
Ignore multiple /
"""

        # @type query string
        splitted = (query.strip('/')).split('/')
        page = ''
        param_list = {}
        i = 0

        # Parsing, finding        page name  page name
        while i < len(splitted) and not('-' in splitted[i]):
            if page != '' and splitted[i] != '':
                page = page + '/'
            page = page + splitted[i]
            i = i+1

        # Parsing, finding page parameters
        while i < len(splitted):
            if '-' in splitted[i]:
                p = splitted[i].split('-')
                param_list[p[0]] = p[1]
            i = i+1

        return page,param_list


    def init_current_request(self):
        request = self.find_action()
        if request == None:
            request = self.find_page()

        return request


    def init_session(self):
        self.session = None
        if "session_key" in self.cookies:
            self.session = SessionManager.get_by_key(self.cookies["session_key"])

        if self.session is None:
            self.session = SessionManager.create_session()

    def init_language(self):
        language = Language()
        if "lang" in self.query:
            if self.query["lang"][0] == "default":
                language.find_preferred(self.preferred_langs)
            else:
                language.set_by_code(self.query["lang"][0])

        self.session.set_language(language)
*/
    private void initSession() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void initLanguage() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Request initCurrentRequest() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
