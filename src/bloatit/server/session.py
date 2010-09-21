from bloatit.server.language import Language

class Session:

    def __init__(self):
        self.language = Language()

    def get_language(self):
        return self.language