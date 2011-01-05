package com.bloatit.web.server;

public interface Text {


    public abstract void writeLine(String newText);
    public abstract void writeRawText(String newText);
    public abstract void writeNewLineChar();
    public abstract void writeIndentation();

    public abstract void indent();

    public abstract void unindent();

}
