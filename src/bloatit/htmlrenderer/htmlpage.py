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

from bloatit.actions.logoutaction import LogoutAction
from bloatit.htmlrenderer.pagecontent.indexcontent import IndexContent
from bloatit.htmlrenderer.pagecontent.logincontent import LoginContent
from bloatit.htmlrenderer.htmltools import HtmlTools
from bloatit.htmlrenderer.indentedtext import IndentedText
"""TODO: prévoir une option de configuration pour générer un mode compact"""

class HtmlPage:
    
    def __init__(self, session):
        self.session = session
        self.html = IndentedText()
        self.design = "/resources/css/design.css"

    def generate_page(self, content):
        self.content = content
        self.html.write('<?xml version=\"1.0\" encoding=\"UTF-8\"?>')
        self.html.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">')
        self.html.write('<html xmlns="http://www.w3.org/1999/xhtml">')
        self.html.indent()
        self.generate_head()
        self.generate_body()
        self.html.unindent()
        self.html.write('</html>')

        return "Content-Type: text/html\r\n\r\n"+self.html.get_text()

    def generate_head(self):
        self.html.write('<head>')
        self.html.indent()
        self.html.write('<metahttp-equiv="content-type" content="text/html;charset=utf-8"/>')
        self.html.write('<link rel="stylesheet" href="'+self.design+'" type="text/css" media="handheld, all" />')

        self.html.write('<title>BloatIt - '+self.content.get_title()+'</title>')
        self.html.unindent()
        self.html.write('</head>')

    def generate_body(self):
        self.html.write('<body>')
        self.html.indent()
        self.html.write('<div id="page">')
        self.html.indent()
        self.generate_top_bar()
        self.generate_title()
        self.html.write('<div id="center">')
        self.html.indent()
        self.generate_main_menu()
        self.generate_content()
        self.html.unindent()
        self.html.write('</div>')
        self.generate_footer()
        self.html.unindent()
        self.html.write('</div>')
        self.html.unindent()
        self.html.write('</body>')


    def generate_logo(self):
        return '<span class="logo_bloatit"><span class="logo_bloatit_bloat">Bloat</span><span class="logo_bloatit_it">It</span></span>'


    def generate_title(self):
        self.html.write('<h1>'+HtmlTools.generate_link(self.session,self.generate_logo(), IndexContent)+'</h1>')

    def generate_top_bar(self):
        self.html.write('<div id="top_bar">')
        self.html.indent()
        if self.session.get_login() is None :
            self.html.write(HtmlTools.generate_link(self.session,self.session._("Login / Signup"), LoginContent))
        else:
            self.html.write(self.session.get_login()+" "+HtmlTools.generate_action_link(self.session,self.session._("Logout"), LogoutAction(self.session)))
        self.html.unindent()
        self.html.write('</div>')

    def generate_main_menu(self):
        
        self.html.write('<div id="main_menu">')
        self.html.indent()
        self.html.write('<ul>')
        self.html.indent()
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Demands"), IndexContent)+'</li>')
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Projects"), IndexContent)+'</li>')
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Groups"), IndexContent)+'</li>')
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Members"), IndexContent)+'</li>')
        self.html.unindent()
        self.html.write('</ul>')
        self.html.write('<ul>')
        self.html.indent()
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Contact"), IndexContent)+'</li>')
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Documentation"), IndexContent)+'</li>')
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("About BloatIt"), IndexContent)+'</li>')
        self.html.write('<li>'+HtmlTools.generate_link(self.session,self.session._("Press"), IndexContent)+'</li>')
        self.html.unindent()
        self.html.write('</ul>')
        self.html.unindent()
        self.html.write('</div>')

    def generate_footer(self):
        self.html.write('<div id="footer">')
        self.html.indent()
        self.html.write(self.session._("This website is under GNU Affero Public Licence."))
        self.html.unindent()
        self.html.write('</div>')

    def generate_content(self):
        self.html.write('<div id="body_content">')
        self.html.indent()
        self.content.generate_body(self.html)
        self.html.unindent()
        self.html.write('</div>')

