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

from bloatit.web.htmlrenderer.page import Page

class MyAccountContent(Page):

    def get_code(self):
        return "my_account"

    def __init__(self, session, parameters={}):
        super(MyAccountContent, self).__init__(session, parameters)

    def get_title(self):
        if self.session.get_auth_token():
            return "My account - "+self.session.get_auth_token().get_member().get_login()
        else:
            return "My account - No account"
    
    def generate_content(self):
        # @type text IndentedText

        if self.session.get_auth_token():

            member = self.session.get_auth_token().get_member()
            # @type member Member


            self.html_result.write("<h2>"+member.get_full_name()+"</h2>")
            self.html_result.write("<p>Full name: "+member.get_full_name()+"</p>")
            self.html_result.write("<p>Login: "+member.get_login()+"</p>")
            self.html_result.write("<p>Email: "+member.get_email()+"</p>")
            self.html_result.write("<p>Karma: "+str(member.get_karma())+"</p>")

        else:
            self.html_result.write("<h2>No account</h2>")
