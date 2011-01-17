package com.bloatit.web.server;

import java.io.OutputStream;

public final class IndentedHtmlText extends Text {
    private int indentCount;
    private final String indentSeparator;
    private final String lineSeparator;

    public IndentedHtmlText(final OutputStream out) {
        super(out);
        this.indentSeparator = "  ";
        this.lineSeparator = "\n";
        this.indentCount = 0;
    }

    @Override
    public final void writeLine(String newText) {
        append(newText);
        writeNewLineChar();
    }

    @Override
    public final void writeIndentation() {
        for (int i = 0; i < indentCount; ++i) {
            append(indentSeparator);
        }
    }

    @Override
    public final void writeNewLineChar() {
        append(lineSeparator);
    }

    @Override
    public final void writeRawText(final String newText) {
        append(newText);
    }

    @Override
    public final void indent() {
        indentCount++;
    }

    @Override
    public final void unindent() {
        if (indentCount > 0) {
            indentCount--;
        }
    }

}
