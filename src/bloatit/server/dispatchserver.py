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

from bloatit.server.language import Language
from bloatit.htmlrenderer.pagecontent.pagenotfoundcontent import PageNotFoundContent
from bloatit.htmlrenderer.pagecontent.indexcontent import IndexContent
from bloatit.htmlrenderer.pagecontent.logincontent import LoginContent
from bloatit.htmlrenderer.htmlpage import HtmlPage
from bloatit.server.session import Session

class DispatchServer:
    page_map = {
    'index': IndexContent,
    'login': LoginContent}

    def __init__(self, query, preferred_langs):
        self.query = query
        self.preferred_langs = preferred_langs

        
    def process(self):



        self.session = Session()

        language = Language()

        if "lang" in self.query:
            if self.query["lang"][0] == "default":
                language.find_preferred(self.preferred_langs)
            else:
                language.set_by_code(self.query["lang"][0])

        self.session.set_language(language)
        


        page = HtmlPage(self.session)

        content = self.find_content()
        return page.generate_page(content)

    def find_content(self):
        "page" in self.query #TODO code mort ?
        self.query["page"][0] in self.page_map
        if "page" in self.query and self.query["page"][0] in self.page_map:
            return self.page_map[self.query["page"][0]](self.session)
        else:
            return PageNotFoundContent(self.session)