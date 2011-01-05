package com.bloatit.web.server;

public abstract class SimpleHtmlText implements Text {

    protected abstract void append(String text);

    @Override
    public void writeLine(String newText) {
        append(newText);
    }

    @Override
    public void writeIndentation() {
        // Do nothing
    }

    @Override
    public void writeNewLineChar() {
        // Do nothing
    }

    @Override
    public void writeRawText(final String newText) {
        append(newText);
    }

    @Override
    public void indent() {
        // Do nothing
    }

    @Override
    public void unindent() {
        // Do nothing
    }
}
