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

class HtmlTools:
    
   
    @classmethod
    def generate_logo(cls):
        return '<span class="logo_bloatit"><span class="logo_bloatit_bloat">Bloat</span><span class="logo_bloatit_it">It</span></span>'

    @classmethod
    def generate_link(cls, session, text, link_page):
        # @type link_page PageContent
        return '<a href="/'+session.get_language().get_code()+'/'+link_page.get_code()+'">'+text+'</a>'

    @classmethod
    def generate_action_link(cls, session, text, link_action):
        # @type link_action Action
        return '<a href="/'+session.get_language().get_code()+'/action/'+link_action.get_code()+'">'+text+'</a>'

    @classmethod
    def generate_url(cls, session, url_page):
        # @type url_page PageContent
        return '/'+session.get_language().get_code()+'/'+url_page.get_code()


    def cut_number(number):
        result = number
        if len(result) > 2:
                if result[1] == ".":
                    if result[2] == "0":
                        result = result[0]
                    else:
                        result = result[:3]
                elif result[2] == ".":
                    result = result[:2]
                else:
                    result = result[:3]
        return result

    @classmethod
    def compress_karma(cls, karma):
        abs_karma = abs(karma)

        if abs_karma < 1000:
            result = str(abs_karma)
        elif abs_karma < 1000000:
            result = str(abs_karma/1000)
            result = cls.cut_number(result)+"k"
        elif abs_karma < 1000000000:
            result = str(abs_karma/1000000)
            result = cls.cut_number(result)+"M"
        elif abs_karma < 1000000000000:
            result = str(abs_karma/1000000000)
            result = cls.cut_number(result)+"T"
        else:
            result = "∞"

        
        if(karma >= 0):
            return result
        else:
            return "-"+result


    
        
