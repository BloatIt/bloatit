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
from bloatit.web.htmlrenderer.pagecontent.pagecontent import Page
from bloatit.web.htmlrenderer.htmltools import HtmlTools


class DemandContent(Page):
    # @type demand Demand

    def __init__(self, session, parameters={}, demand=None):
        # @type session Session
        
        self.session = session
        self.parameters = parameters
        if demand is None:
            if 'id' in parameters:
                try:
                    id = int(parameters['id'])
                    self.demand = DemandManager.get_demand_by_id(id)
                except ValueError:
                    pass # Back to general case : no demand specified
        else:
            self.demand = demand

    def get_code(self):
        if self.demand != None:
            return 'demand/id-'+ str(self.demand.get_id()) + '/title-' + HtmlTools.escape_url_string(self.demand.get_title())
        else:
            return 'demand' # TODO Faire un système pour afficher une page d'erreur

    def generate_body(self, text):
        # @type text IndentedText
        print('plop')
        if self.demand is None:
            self._generate_empty_body(text)
        else :
            self._generate_not_empty_body(text)

    def _generate_empty_body(self, text):
        text.write('Error : Specified demand Id incorrect') #TODO

    def _generate_not_empty_body(self, text):
        text.write('<div class="demand">')
        text.indent()
        text.write('<p>Demand id : '+ str(self.demand.get_id()) + '</p>' )
        text.write('<p>Nota : demand Id is always 1 (testing)</p>')
        text.unindent()
        text.write('</div>')

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
