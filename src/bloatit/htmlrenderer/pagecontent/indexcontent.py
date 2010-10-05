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

from bloatit.htmlrenderer.htmltools import HtmlTools
from bloatit.htmlrenderer.pagecontent.pagecontent import PageContent

class IndexContent(PageContent):

    def get_code(self):
        return "index"

    
    def __init__(self, session):
        self.session = session


    def get_title(self):
        return "Finance free software"

    def generate_body(self, text):
        text.write("<h2>Welcome in "+HtmlTools.generate_logo()+" website</h2>")
        text.write(HtmlTools.generate_logo()+" is a wonderful website !")
        text.write(100*" is a wonderful website !")