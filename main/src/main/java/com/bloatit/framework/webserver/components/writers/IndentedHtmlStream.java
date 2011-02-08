package com.bloatit.framework.webserver.components.writers;

import java.io.OutputStream;

/**
 * <p>
 * An implementation of {@link HtmlStream} that use indentation.
 * </p>
 * <p>
 * Should be used in debug and build environments
 * </p>
 */
public final class IndentedHtmlStream extends HtmlStream {
    private int indentCount;
    private final String indentSeparator;
    private final String lineSeparator;

    /**
     * Creates a new IndentedHtmlStream
     * 
     * @param out
     */
    public IndentedHtmlStream(final OutputStream out) {
        super(out);
        this.indentSeparator = "  ";
        this.lineSeparator = "\n";
        this.indentCount = 0;
    }

    @Override
    public final void writeLine(final String newText) {
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
