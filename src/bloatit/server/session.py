from bloatit.server.language import Language

class Session:

    def __init__(self):
        pass

    def get_language(self):
        return self.language

    def set_language(self,language):
        self.language = language
        self._ = language.get_gettext()
        