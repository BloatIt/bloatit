package com.bloatit.web.server;

public interface Text {

    public abstract void writeLine(String newText);

    public abstract void indent();

    public abstract void unindent();

}
