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

from bloatit.htmlrenderer.pagecontent.indexcontent import IndexContent
from bloatit.actions.action import Action
from bloatit.htmlrenderer.htmltools import HtmlTools


class LoginAction(Action):

    def get_code(self):
        return "login"

    def get_login_code(self):
        return "bloatit_login"

    def get_password_code(self):
        return "bloatit_password"

    def process(self, query, post):
        if self.get_login_code() in post and self.get_password_code() in post:
            if post[self.get_password_code()][0] == "pass":
                return "Set-Cookie: UserID="+post[self.get_login_code()][0]+"; path=/; Max-Age=31536000; Version=1 \r\nLocation: "+HtmlTools.generate_url(self.session,IndexContent)+"\r\n\r\n"
        return "Location: "+HtmlTools.generate_url(self.session,IndexContent)+"\r\n\r\n"
