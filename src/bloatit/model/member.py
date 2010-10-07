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

class Member:

    def __init__(self):
        self.login = ""
        self.email = ""
        self.full_name = ""
        self.id = -1

    def get_login(self):
        return self.login

    def get_email(self):
        return self.email

    def get_full_name(self):
        return self.full_name

    def get_id(self):
        return self.id

    def get_karma(self):
        return self.karma