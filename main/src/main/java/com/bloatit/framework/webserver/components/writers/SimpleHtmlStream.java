package com.bloatit.framework.webserver.components.writers;

import java.io.OutputStream;

/**
 * <p>
 * An implementation of an {@link QueryResponseStream} that just display raw
 * text (no indentation, no line breaks ...)
 * </p>
 * <p>
 * Should be used in production environments
 * </p>
 */
public final class SimpleHtmlStream extends QueryResponseStream {

    /**
     * Creates a new SimpleHtmlStream
     * 
     * @param out the underlying stream
     */
    public SimpleHtmlStream(final OutputStream out) {
        super(out);
    }

    @Override
    public void writeLine(final String newText) {
        append(newText);
    }

    /**
     * Nothing
     */
    @Override
    public void writeIndentation() {
        // Do nothing
    }

    /**
     * Nothing
     */
    @Override
    public void writeNewLineChar() {
        // Do nothing
    }

    @Override
    public void writeRawText(final String newText) {
        append(newText);
    }

    /**
     * Nothing
     */
    @Override
    public void indent() {
        // Do nothing
    }

    /**
     * Nothing
     */
    @Override
    public void unindent() {
        // Do nothing
    }
}
