#!/usr/bin/env python  



from elveos import server
from elveos.auth.guiauthenticator import GuiAuthenticator
from elveos.member import Member
server.set_host('http://127.0.0.1')
server.authenticate(GuiAuthenticator().authenticate())


for member in Member.objects:
    print member.name + (member.email and " "+member.email or "")
    











