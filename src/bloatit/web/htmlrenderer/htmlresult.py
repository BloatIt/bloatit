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

from bloatit.web.htmlrenderer.indentedtext import IndentedText

class HtmlResult(IndentedText):

    def __init__(self, session):
        IndentedText.__init__(self)
        self.redirect = None
        self.session=session

    def set_redirect(self,location):
        self.redirect = location

    def set_content(self, has_content):
        self.has_content = has_content

    def generate(self):
        result = "Set-Cookie: session_key="+self.session.get_key()+"; path=/; Max-Age=1296000; Version=1 \r\n"


        if self.redirect:
            result += "Location: "+self.redirect+"\r\n"

        text = self.get_text()
        if len(text):
            result += "Content-Type: text/html\r\n"

        result += "\r\n"
        result += text

        return result
