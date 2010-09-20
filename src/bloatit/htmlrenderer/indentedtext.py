class IndentedText:

    def __init__(self):
        self.indent_count=0
        self.indent_separator="  "
        self.line_separator="\n"
        self.text=""

    def indent(self):
        self.indent_count+=1
        
    def unindent(self):
        self.indent_count-=1

    def write(self, text):
        self.text += self.indent_separator*self.indent_count + text + self.line_separator

    def get_text(self):
        return self.text