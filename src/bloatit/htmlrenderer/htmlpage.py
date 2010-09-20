# -*- coding: utf-8 -*-

from bloatit.htmlrenderer.indentedtext import IndentedText

"""TODO: prévoir une option de configuration pour générer un mode compact"""

class HtmlPage:
    
    def __init__(self):
        self.html = IndentedText()
        self.title = "UntitlePage"
        self.design = "/resources/css/design.css"

    def generate_page(self):
        self.html.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">')
        self.html.write('<html xmlns="http://www.w3.org/1999/xhtml">')
        self.html.indent()
        self._generate_head()
        self._generate_body()
        self.html.unindent()
        self.html.write('</html>')

        return self.html.get_text()

    def _generate_head(self):
        self.html.write('<head>')
        self.html.indent()
        self.html.write('<link rel="stylesheet" href="'+self.design+'" type="text/css" media="handheld, all" />')

        self.html.write('<title>'+self.title+'</title>')
        self.html.unindent()
        self.html.write('</head>')

    def _generate_body(self):
        self.html.write('<body>')
        self.html.indent()
        self.html.write('<h1>'+self._generate_logo()+'</h1>')
        self.html.write('Bienvenue sur le site de '+self._generate_logo()+'.')
        self.html.unindent()
        self.html.write('</body>')


    def _generate_logo(self):
        return '<span class="logo_bloatit"><span class="logo_bloatit_bloat">Bloat</span><span class="logo_bloatit_it">It</span></span>'




