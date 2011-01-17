package com.bloatit.web.server;

public abstract class IndentedHtmlText implements Text {
    private int indentCount;
    private final String indentSeparator;
    private final String lineSeparator;

    public IndentedHtmlText() {
        this.indentSeparator = "  ";
        this.lineSeparator = "\n";
        indentCount = 0;
    }

    protected abstract void append(String text);

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
