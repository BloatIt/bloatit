# -*- coding: utf-8 -*-

# Copyright (C) 2010 BloatIt.
#
# This file is part of BloatIt.
#
# BloatIt is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# BloatIt is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with BloatIt. If not, see <http://www.gnu.org/licenses/>.

from bloatit.htmlrenderer.htmlresult import HtmlResult
from bloatit.server.sessionmanager import SessionManager
from bloatit.htmlrenderer.pagecontent.demandcontent import DemandContent
from bloatit.actions.logoutaction import LogoutAction
from bloatit.actions.loginaction import LoginAction
from bloatit.server.language import Language
from bloatit.htmlrenderer.pagecontent.pagenotfoundcontent import PageNotFoundContent
from bloatit.htmlrenderer.pagecontent.indexcontent import IndexContent
from bloatit.htmlrenderer.pagecontent.logincontent import LoginContent
from bloatit.htmlrenderer.htmlpage import HtmlPage
from bloatit.server.session import Session

class DispatchServer:
    page_map = {
    'index': IndexContent,
    'login': LoginContent,
    'demands' : DemandContent
    }

    action_map = {
    'login': LoginAction,
    'logout': LogoutAction}

    def __init__(self, query, post, cookies, preferred_langs):
        self.query = query
        self.post = post
        self.cookies = cookies
        self.preferred_langs = preferred_langs

        
    def process(self):

        self.session = None
        #print(self.query)
        if "session_key" in self.cookies:
            self.session = SessionManager.get_by_key(self.cookies["session_key"])

        if self.session is None:
            self.session = SessionManager.create_session()


        html_result = HtmlResult(self.session)

        language = Language()

        if "lang" in self.query:
            if self.query["lang"][0] == "default":
                language.find_preferred(self.preferred_langs)
            else:
                language.set_by_code(self.query["lang"][0])

        self.session.set_language(language)

        action = self.find_action()
        if action != None:
            action.process(html_result, self.query, self.post)
        else:
            page = HtmlPage(self.session, html_result)

            content = self.find_content()
            page.generate_page(content)
        return html_result.generate()

    def find_action(self):
        if "page" in self.query:
            query = self.query["page"][0]
            if query.startswith("action/") and query[7:] in self.action_map:
                return self.action_map[query[7:]](self.session)
        return None

    def find_content(self):
        
        if "page" in self.query:
            query = self.query["page"][0]
            if query in self.page_map:
                return self.page_map[query](self.session)
        
        return PageNotFoundContent(self.session)