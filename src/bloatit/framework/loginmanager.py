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

from bloatit.framework.authtoken import AuthToken
from bloatit.framework.membermanager import MemberManager
import hashlib
import random
import string

class LoginManager:

    auth_token_list = {}
    sha = hashlib.sha256()
    accounts = {
    'fred' : 'lapin',
    'yoann' : 'babar',
    'tom' : 'savoie',
    }

    @classmethod
    def login_by_password(cls, login, password):
        if login in cls.accounts and cls.accounts[login] == password:
            return cls.new_auth_token(login)
        else:
            return None

    @classmethod
    def new_auth_token(cls, login):

        member = MemberManager.get_member_by_login(login)
        #TODO: throw exception if no member match

        
        d = "".join([random.choice(string.ascii_letters) for x in range(100)])
        cls.sha.update(d.encode())

        token_key = cls.sha.hexdigest()
        new_token = AuthToken(member, token_key)
        
        return new_token

    @classmethod
    def get_by_key(cls, key):
        if key in cls.auth_token_list:
            return cls.auth_token_list[key]
        else:
            return None