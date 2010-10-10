from bloatit.model.member import Member
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

class MemberManager:

    members = []

    member = Member()
    member.login = "fred"
    member.full_name = "Fred"
    member.email = "fred@bloatit.com"
    member.karma = 2500
    members.append(member)
    
    member = Member()
    member.login = "tom"
    member.full_name = "Tom"
    member.email = "tom@bloatit.com"
    member.karma = 20
    members.append(member)
    
    member = Member()
    member.login = "yoann"
    member.full_name = "Yoann"
    member.email = "yoann@bloatit.com"
    member.karma = -3
    members.append(member)


    @classmethod
    def get_member_by_login(cls, login):
        for member in cls.members:
            if login == member.get_login():
                return member

        return None


