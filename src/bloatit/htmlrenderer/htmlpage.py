# -*- coding: utf-8 -*-

from bloatit.htmlrenderer.indentedtext import IndentedText

"""TODO: prévoir une option de configuration pour générer un mode compact"""

class HtmlPage:
    title = "UntitlePage"
    html = IndentedText()


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
        self.html.write('<title>'+self.title+'</title>')
        self.html.unindent()
        self.html.write('</head>')

    def _generate_body(self):
        self.html.write('<body>')
        self.html.indent()
        self.html.write('Bienvenue sur le site de BloatIt')
        self.html.unindent()
        self.html.write('</body>')







