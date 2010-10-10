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

from bloatit.web.htmlrenderer.pagecontent.indexcontent import IndexContent
from bloatit.web.actions.action import Action
from bloatit.web.htmlrenderer.htmltools import HtmlTools


class LogoutAction(Action):

    def get_code(self):
        return "logout"

    
    def process(self, html_result, query, post):
        self.session.set_logged(False)
        self.session.set_auth_token(None)

        html_result.set_redirect(HtmlTools.generate_url(self.session,IndexContent(self.session)))

        
