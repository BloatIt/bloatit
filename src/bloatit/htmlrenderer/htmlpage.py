# -*- coding: utf-8 -*-

from bloatit.htmlrenderer.indentedtext import IndentedText

"""TODO: prévoir une option de configuration pour générer un mode compact"""

class HtmlPage:
    
    def __init__(self, session):
        self.session = session
        self.html = IndentedText()
        self.design = "/resources/css/design.css"

    def generate_page(self, content):
        self.content = content
        self.html.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">')
        self.html.write('<html xmlns="http://www.w3.org/1999/xhtml">')
        self.html.indent()
        self.generate_head()
        self.generate_body()
        self.html.unindent()
        self.html.write('</html>')

        return self.html.get_text()

    def generate_head(self):
        self.html.write('<head>')
        self.html.indent()
        self.html.write('<link rel="stylesheet" href="'+self.design+'" type="text/css" media="handheld, all" />')

        self.html.write('<title>BloatIt - '+self.content.get_title()+'</title>')
        self.html.unindent()
        self.html.write('</head>')

    def generate_body(self):
        self.html.write('<body>')
        self.html.indent()
        self.generate_top_bar()
        self.generate_title()
        self.generate_main_menu()
        self.generate_content()
        self.html.unindent()
        self.html.write('</body>')


    def generate_logo(self):
        return '<span class="logo_bloatit"><span class="logo_bloatit_bloat">Bloat</span><span class="logo_bloatit_it">It</span></span>'


    def generate_title(self):
        self.html.write('<h1>'+self.generate_logo()+'</h1>')

    def generate_top_bar(self):
        self.html.write('<div class="top_bar">')
        self.html.indent()
        self.html.write('<a href="/fr/login">Login</a>')
        self.html.write('<a href="/">Help</a>')
        self.html.write('<span class="top_bar_search_field"><input type="text" value="" /></span><span class="top_bar_search_button"><input type="submit"  value="Search"/></span>')
        self.html.unindent()
        self.html.write('</div>')

    def generate_main_menu(self):
        self.html.write('<div class="main_menu">')
        self.html.indent()
        self.html.write('<ul>')
        self.html.indent()
        self.html.write('<li><a href="/">Demands</a></li>')
        self.html.write('<li><a href="/">Projects</a></li>')
        self.html.write('<li><a href="/">Groups</a></li>')
        self.html.write('<li><a href="/">Members</a></li>')
        self.html.unindent()
        self.html.write('</ul>')
        self.html.write('<ul>')
        self.html.indent()
        self.html.write('<li><a href="/">Contact</a></li>')
        self.html.write('<li><a href="/">Documentation</a></li>')
        self.html.write('<li><a href="/">About BloatIt</a></li>')
        self.html.write('<li><a href="/">Press</a></li>')
        self.html.unindent()
        self.html.write('</ul>')
        self.html.unindent()
        self.html.write('</div>')

    def generate_content(self):
        self.html.write('<div class="body_content">')
        self.html.indent()
        self.content.generate_body(self.html)
        self.html.unindent()
        self.html.write('</div>')

