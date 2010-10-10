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

import bloatit.web.actions.loginaction
from bloatit.web.htmlrenderer.htmlcomponent.htmlbutton import HtmlButton
from bloatit.web.htmlrenderer.htmlcomponent.htmlpasswordfield import HtmlPasswordField
from bloatit.web.htmlrenderer.htmlcomponent.htmltextfield import HtmlTextField
from bloatit.web.htmlrenderer.htmlcomponent.htmlform import HtmlForm
from bloatit.web.htmlrenderer.pagecontent.pagecontent import PageContent

class LoginContent(PageContent):

    def get_code(self):
        return "login"

    def __init__(self, session, parameters={}):
        self.session = session
        self.parameters = parameters


    def get_title(self):
        return "Login or sigup"

    def generate_body(self, text):
        # @type text IndentedText
        login_form = HtmlForm()
        login_field = HtmlTextField()
        password_field = HtmlPasswordField()
        submit_button = HtmlButton()

        login_form.add_component(login_field)
        login_form.add_component(password_field)
        login_form.add_component(submit_button)

        login_action = bloatit.web.actions.loginaction.LoginAction(self.session)
        
        login_form.set_action(bloatit.web.actions.loginaction.LoginAction(self.session))
        submit_button.set_label(self.session._("Login"))
        login_field.set_name(login_action.get_login_code())
        password_field.set_name(login_action.get_password_code())


        text.write("<h2>"+self.session._("Login")+"</h2>")
        login_form.generate(text)
        text.write("<h2>"+self.session._("Sigup")+"</h2>")
        text.write("<p>Not yet implemented.</p>")