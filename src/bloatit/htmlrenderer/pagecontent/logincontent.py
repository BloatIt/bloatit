from bloatit.htmlrenderer.pagecontent.pagecontent import PageContent

class LoginContent(PageContent):

    def get_code():
        return "login"

    def __init__(self, session):
        self.session = session


    def get_title(self):
        return "Login or sigup"

    def generate_body(self, text):
        text.write("<h2>"+self.session._("Login")+"</h2>")
        text.write("<h2>"+self.session._("Sigup")+"</h2>")