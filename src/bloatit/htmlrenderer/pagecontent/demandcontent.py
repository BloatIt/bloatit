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

from bloatit.framework.demandmanager import DemandManager
from bloatit.htmlrenderer.htmltools import HtmlTools
from bloatit.htmlrenderer.pagecontent.pagecontent import PageContent
import urllib

class DemandContent(PageContent):

    def __init__(self, session, parameters={}, demand=None):
        # @type session Session
        # @type demand Demand
        self.session = session
        self.parameters = parameters
        if demand is None:
            if 'id' in parameters:
                self.demand = DemandManager.get_demand_by_id(parameters['id'])
        else:
            self.demand = demand


    def get_code(self):
        if self.demand != None:
            return 'demand/id-'+ str(self.demand.get_id()) + '/title-' + urllib.parse.quote_plus(self.demand.get_title())
        else:
            return 'TODO' # TODO Faire un système pour afficher une page d'erreur

    def generate_list_field(self, text):
        # @type text IndentedText
        text.write('<div class="demand_entry">')
        text.indent()
        text.write('<p class="demand_title">'+ HtmlTools.generate_link(self.session, self.demand.get_title() , self) +'</p>')
        text.write('<p class="demand_description">'+ self.demand.get_description()+'</p>')
        text.unindent()
        text.write('</div>')

    def get_title (self):
        return 'TODO'