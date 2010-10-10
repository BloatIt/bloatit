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

from bloatit.web.htmlrenderer.htmltools import HtmlTools
from bloatit.web.pages.indexcontent import IndexContent
from bloatit.web.server.request import Request

class Action(Request):

    def __init__(self, session, parameters={}):
        # @type session Session
        self.session = session
        self.parameters = parameters


    def get_url(self):
        return '/'+self.session.get_language().get_code()+'/action/'+self.get_code()

    def get_code(self):
        """return the action code"""
        pass

    def _process(self):
        # @type html_result HtmlResult
        self.html_result.set_redirect(HtmlTools.generate_url(self.session,IndexContent))
        