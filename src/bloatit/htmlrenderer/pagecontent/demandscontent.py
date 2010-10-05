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
# but WITHOUT ANY WARRANTY; without even the implslied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with BloatIt. If not, see <http://www.gnu.org/licenses/>.

from bloatit.htmlrenderer.elementrenderer.demandlistrenderer import DemandListRenderer
from bloatit.framework.demandmanager import DemandManager
from bloatit.htmlrenderer.pagecontent.pagecontent import PageContent

class DemandsContent(PageContent):

    def __init__(self, session):
        self.session = session

    def get_title(self):
        return "Handle demands"

    def get_code(self):
        return "demands"

    def generate_body(self, text):
        # @type text IndentedText
        demands = DemandManager.get_demands()
        demands_renderer = DemandListRenderer(self.session, demands)
        
        demands_renderer.generate(text)