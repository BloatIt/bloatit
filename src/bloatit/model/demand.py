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

class Demand:

    def __init__(self):
        self.title = "Demand_Title"
        self.description = "Ceci est une description"
        self.specification = "Ceci est une spécification complète"
        self.id = 3

    def get_title(self):
        return self.title

    def get_description(self):
        return self.description

    def get_specification(self):
        return self.specification

    def get_id(self):
        return self.id