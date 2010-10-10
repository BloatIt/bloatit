import hashlib
import string
import random
from bloatit.web.server.session import Session
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

class SessionManager:

    active_session = {}
    sha = hashlib.sha256()


    def __init__(self):
        """Documentation"""

    @classmethod
    def create_session(cls):
        new_session = Session()
        d = "".join([random.choice(string.ascii_letters) for _ in range(100)])
        cls.sha.update(d.encode())

        session_key = cls.sha.hexdigest()
        new_session.set_key(session_key)
        cls.active_session[session_key] = new_session

        return new_session

    @classmethod
    def get_by_key(cls, key):
        if key in cls.active_session:
            return cls.active_session[key]
        else:
            return None
        
