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

from bloatit.web.htmlrenderer.htmlcomponent.htmlcontainer import HtmlContainer

class HtmlForm(HtmlContainer):

    def generate(self, text):
        # @type text IndentedText
        text.write('<form action="'+self.action.get_url()+'" method="POST">')
        text.indent()

        #Generate content fields
        super(HtmlForm, self).generate(text)
        text.unindent()
        text.write('</form>')

    def set_action(self, action):
        # @type action Action
        self.action = action