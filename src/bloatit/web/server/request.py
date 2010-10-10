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

class Request:
    
    def __init__(self, session, parameters={}):
        self.session = session
        self.parameters = parameters
    
    def _process(self):
        pass
        
    def do_process(self, html_result, query='', post=''):
        # TODO : Remove query/post and use the parameters list from constructor
        self.html_result = html_result
        self.query = query
        self.post = post
        self._process()
    
    def get_code(self):
        pass
    
    