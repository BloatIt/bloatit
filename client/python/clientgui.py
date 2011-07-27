#!/usr/bin/env python  

import gtk  
import webkit  
import gobject  

gobject.threads_init()  
window = gtk.Window()
window.set_default_size(800, 600)
window.connect("destroy", lambda a: gtk.main_quit()) 
browser = webkit.WebView()  
browser.open("https://elveos.org")  
window.add(browser)  
window.show_all()  
gtk.main()  
