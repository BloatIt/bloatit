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

from bloatit.htmlrenderer.pagecontent.demandscontent import DemandsContent
from bloatit.htmlrenderer.pagecontent.demandcontent import DemandContent
from bloatit.actions.logoutaction import LogoutAction
from bloatit.htmlrenderer.pagecontent.indexcontent import IndexContent
from bloatit.htmlrenderer.pagecontent.logincontent import LoginContent
from bloatit.htmlrenderer.htmltools import HtmlTools
from bloatit.htmlrenderer.indentedtext import IndentedText
from bloatit.htmlrenderer.pagecontent.myaccountcontent import MyAccountContent

"""TODO: prévoir une option de configuration pour générer un mode compact"""

class HtmlPage:
    
    def __init__(self, session, html_result):
        self.session = session
        self.html_result = html_result
        self.design = "/resources/css/design.css"

    def generate_page(self, content):
        self.content = content
        self.html_result.write('<?xml version=\"1.0\" encoding=\"UTF-8\"?>')
        self.html_result.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">')
        self.html_result.write('<html xmlns="http://www.w3.org/1999/xhtml">')
        self.html_result.indent()
        self.generate_head()
        self.generate_body()
        self.html_result.unindent()
        self.html_result.write('</html>')


    def generate_head(self):
        self.html_result.write('<head>')
        self.html_result.indent()
        self.html_result.write('<metahttp-equiv="content-type" content="text/html;charset=utf-8"/>')
        self.html_result.write('<link rel="stylesheet" href="'+self.design+'" type="text/css" media="handheld, all" />')

        self.html_result.write('<title>BloatIt - '+self.content.get_title()+'</title>')
        self.html_result.unindent()
        self.html_result.write('</head>')

    def generate_body(self):
        self.html_result.write('<body>')
        self.html_result.indent()
        self.html_result.write('<div id="page">')
        self.html_result.indent()
        self.generate_top_bar()
        self.generate_title()
        self.html_result.write('<div id="center">')
        self.html_result.indent()
        self.generate_main_menu()
        self.generate_content()
        self.html_result.unindent()
        self.html_result.write('</div>')
        self.generate_footer()
        self.html_result.unindent()
        self.html_result.write('</div>')
        self.html_result.unindent()
        self.html_result.write('</body>')


    def generate_logo(self):
        return '<span class="logo_bloatit"><span class="logo_bloatit_bloat">Bloat</span><span class="logo_bloatit_it">It</span></span>'


    def generate_title(self):
        self.html_result.write('<h1>'+HtmlTools.generate_link(self.session,self.generate_logo(), IndexContent(self.session))+'</h1>')

    def generate_top_bar(self):
        self.html_result.write('<div id="top_bar">')
        self.html_result.indent()
        if self.session.is_logged():
            full_name = self.session.get_auth_token().get_member().get_full_name()
            self.html_result.write(HtmlTools.generate_link(self.session,full_name, MyAccountContent(self.session) )+" "+HtmlTools.generate_action_link(self.session,self.session._("Logout"), LogoutAction(self.session)))
        else:
            self.html_result.write(HtmlTools.generate_link(self.session,self.session._("Login / Signup"), LoginContent(self.session) ))
            
        self.html_result.unindent()
        self.html_result.write('</div>')

    def generate_main_menu(self):
        
        self.html_result.write('<div id="main_menu">')
        self.html_result.indent()
        self.html_result.write('<ul>')
        self.html_result.indent()
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Demands"), DemandsContent(self.session) )+'</li>')
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Projects"), IndexContent(self.session) )+'</li>')
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Groups"), IndexContent(self.session) )+'</li>')
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Members"), IndexContent(self.session) )+'</li>')
        self.html_result.unindent()
        self.html_result.write('</ul>')
        self.html_result.write('<ul>')
        self.html_result.indent()
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Contact"), IndexContent(self.session) )+'</li>')
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Documentation"), IndexContent(self.session) )+'</li>')
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("About BloatIt"), IndexContent(self.session) )+'</li>')
        self.html_result.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Press"), IndexContent(self.session) )+'</li>')
        self.html_result.unindent()
        self.html_result.write('</ul>')
        self.html_result.unindent()
        self.html_result.write('</div>')

    def generate_footer(self):
        self.html_result.write('<div id="footer">')
        self.html_result.indent()
        self.html_result.write(self.session._("This website is under GNU Affero Public Licence."))
        self.html_result.unindent()
        self.html_result.write('</div>')

    def generate_content(self):
        self.html_result.write('<div id="body_content">')
        self.html_result.indent()
        self.content.generate_body(self.html_result)
        self.html_result.unindent()
        self.html_result.write('</div>')

