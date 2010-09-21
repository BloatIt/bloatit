# -*- coding: utf-8 -*-

class HtmlTools:
    
   

    def generate_logo():
        return '<span class="logo_bloatit"><span class="logo_bloatit_bloat">Bloat</span><span class="logo_bloatit_it">It</span></span>'

    def generate_link(session, text, link_page):
        return '<a href="/'+session.get_language().get_code()+'/'+link_page.get_code()+'">'+text+'</a>'
