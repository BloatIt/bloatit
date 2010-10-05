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


class Session:

    def __init__(self):
        self.login = None
        self.key = None
        self.logged = False

    def get_language(self):
        return self.language

    def set_language(self,language):
        self.language = language
        self._ = language.get_gettext()

    def set_login(self, login):
        self.login = login

    def get_login(self):
        return self.login

    def set_logged(self, logged):
        self.logged = logged

    def is_logged(self):
        return self.logged

    def set_key(self, key):
        # @type string
        self.key = key

    def get_key(self):
        return self.key