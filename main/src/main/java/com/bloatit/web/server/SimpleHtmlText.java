package com.bloatit.web.server;

import java.io.OutputStream;

public final class SimpleHtmlText extends Text {

    public SimpleHtmlText(final OutputStream out) {
        super(out);
    }

    @Override
    public void writeLine(final String newText) {
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
