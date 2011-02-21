package com.bloatit.framework.webserver.components.writers;

import java.io.IOException;
import java.io.OutputStream;

import com.bloatit.framework.xcgiserver.XcgiRequestAbordedException;

/**
 * <p>
 * An encapsulation of an outputStream used to input HtmlTags.
 * </p>
 * <p>
 * Each class implementing the text interface has its own way to manage
 * indentation and line return.
 * </p>
 */
public abstract class QueryResponseStream {

    private final OutputStream out;

    /**
     * Creates an HtmlStream based on a given outputStream
     * 
     * @param out the underlying stream. Should usually be a socket to a web
     *            server (or an encapsulation of a socket to a web server)
     */
    public QueryResponseStream(final OutputStream out) {
        super();
        this.out = out;
    }

    /**
     * Write a line of text. Some implementations may not add a newLineChar.
     * 
     * @param newText should not finish with a new line char.
     */
    public abstract void writeLine(String newText);

    /**
     * Write raw text. Just write the text to the output.
     * 
     * @param newText is the text to write.
     */
    public abstract void writeRawText(String newText);

    /**
     * Write a new line char. Some implementations may do nothing here.
     */
    public abstract void writeNewLineChar();

    /**
     * Write some space to indent the output. Some implementations may do noting
     * here.
     */
    public abstract void writeIndentation();

    /**
     * Add one to the number of indentation to add on the next
     * {@link #writeIndentation()}
     */
    public abstract void indent();

    /**
     * remove one to the number of indentation to add on the next
     * {@link #writeIndentation()}.
     */
    public abstract void unindent();

    /**
     * ??
     * 
     * @param text
     */
    protected final void append(final String text) {
        try {
            out.write(text.getBytes());
        } catch (final IOException ex) {
            throw new XcgiRequestAbordedException(ex);
        }
    }
}
