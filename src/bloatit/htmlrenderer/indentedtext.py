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

class IndentedText:

    def __init__(self):
        self.indent_count=0
        self.indent_separator="  "
        self.line_separator="\n"
        self.text=""

    def indent(self):
        self.indent_count+=1
        
    def unindent(self):
        self.indent_count-=1

    def write(self, text):
        self.text += self.indent_separator*self.indent_count + text + self.line_separator

    def get_text(self):
        return self.text