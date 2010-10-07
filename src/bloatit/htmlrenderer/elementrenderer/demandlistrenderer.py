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

from bloatit.htmlrenderer.pagecontent.demandcontent import DemandContent
from bloatit.htmlrenderer.htmltools import HtmlTools
from bloatit.model.demand import Demand
from bloatit.htmlrenderer.htmlcomponent.htmlcomponent import HtmlComponent

class DemandListRenderer(HtmlComponent):
    """Renders a list of demand as Html"""

    def __init__(self, session, demands ):
        """
        demands - a list of demands
        """
        self.demands = demands
        self.session = session

    def generate(self, text):
        # @type text IndentedText
        text.write('<div class="demand_table">')
        text.indent()
        for demand in self.demands:
            # @type demand Demand
            demand_view = DemandContent(self.session, demand=demand)
            demand_view.generate_list_field(text)
            
        text.unindent()
        text.write('</div>')