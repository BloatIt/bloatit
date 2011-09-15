package com.bloatit.framework.webprocessor.components.meta;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.FrameworkConfiguration;

public abstract class Node<T extends Node<T>> implements Iterable<T>, Cloneable {

    // -------------------------------------------------------------------------------
    protected static final byte[] newLine = "\n".getBytes();
    // 50 char ' ' ///
    protected static final byte[] indent = "                                              ".getBytes();
    protected static int indentSize = 1;

    protected static final void writeIndentation(final OutputStream out) throws IOException {
        out.write(indent, 0, indentSize);
    }

    protected static final void indent() {
        indentSize++;
        if (indentSize > 49) {
            indentSize = 49;
        }
    }

    protected static final void unindent() {
        indentSize--;
        if (indentSize < 1) {
            indentSize = 1;
        }
    }

    // -------------------------------------------------------------------------------

    protected final HtmlTag tag;

    public Node(String tag) {
        if (tag != null) {
            this.tag = new HtmlTag(tag);
        } else {
            this.tag = null;
        }
    }

    public Node(HtmlTag tag) {
        this.tag = tag;
    }

    /**
     * Return whether this element has at least one child
     * 
     * @return <code>true</code> if this element has at least one child,
     *         <code>false</code> otherwise
     */
    protected final boolean hasChild() {
        return iterator().hasNext();
    }

    /**
     * A method that has to be implemented by all children, and that describes
     * the way it will be represented as an HtmlTag
     * 
     * @param txt the <code>Text</code> that will be used to display the Html
     *            tags
     */
    public void write(final OutputStream out) throws IOException {
        if (tag != null) {
            writeTagAndOffspring(out);
        } else {
            for (final T html : this) {
                html.write(out);
            }
        }
    }

    private void writeTagAndOffspring(final OutputStream out) throws IOException {
        if (FrameworkConfiguration.isHtmlMinified()) {
            writeMinified(out);
        } else {
            writeNonMinified(out);
        }
    }

    private void writeNonMinified(final OutputStream out) throws IOException {
        if (autoCloseTag()) {
            out.write(newLine);
            writeIndentation(out);
            out.write(tag.getSelfClosingTag().getBytes());
        } else {
            indent();
            out.write(newLine);
            writeIndentation(out);
            out.write(tag.getOpenTag().getBytes());
            for (final T html : this) {
                if (html != null) {
                    html.write(out);
                }
            }
            unindent();
            out.write(tag.getCloseTag().getBytes());
        }
        out.write(newLine);
        writeIndentation(out);
    }

    // return !hasChild();
    protected abstract boolean autoCloseTag();

    private void writeMinified(final OutputStream out) throws IOException {
        if (autoCloseTag()) {
            out.write(tag.getSelfClosingTag().getBytes());
        } else {
            out.write(tag.getOpenTag().getBytes());
            for (final T html : this) {
                if (html != null) {
                    html.write(out);
                }
            }
            out.write(tag.getCloseTag().getBytes());
        }
    }

    @Override
    public Node<T> clone() {
        throw new NotImplementedException();
    }

}
