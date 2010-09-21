from bloatit.htmlrenderer.pagecontent.pagecontent import PageContent

class PageNotFoundContent(PageContent):

    def get_code():
        return "pagenotfound"

    def __init__(self, session):
        self.session = session


    def get_title(self):
        return "Page not found"

    def generate_body(self, text):
        text.write("Page not found")