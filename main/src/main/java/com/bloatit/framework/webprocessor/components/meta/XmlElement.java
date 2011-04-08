package com.bloatit.framework.webprocessor.components.meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.components.writers.QueryResponseStream;

public abstract class XmlElement extends XmlNode {
    protected final List<XmlNode> children = new ArrayList<XmlNode>();
    protected final HtmlTag tag;

    public XmlElement(final String tag) {
        super();
        this.tag = new HtmlTag(tag);
    }

    public XmlElement() {
        super();
        this.tag = null;
    }

    public XmlElement(XmlElement element) {
        super();
        for (XmlNode child : element.children) {
            this.children.add(child);
        }
        this.tag = element.tag;
    }

    /**
     * <p>
     * Add an attribute to an element
     * </p>
     * <p>
     * Special treatment will happen if the attribute <code>name</code> is
     * <code>id<code>
     * </p>
     * <p>
     * Example :
     *
     * <pre>
     * HtmlElement e = new HtmlElement(&quot;img&quot;);
     * e.addAttribute(&quot;src&quot;, &quot;example.com/plop.png&quot;);
     *
     * </pre>
     *
     * will be used to create : {@code <img src="example.com/plop.png />}
     * </p>
     *
     * @param name the name of the attribute to add
     * @param value the value of the attribute to add
     * @return itself
     */
    public XmlElement addAttribute(final String name, final String value) {
        if (tag == null) {
            throw new BadProgrammerException("Are you trying to add an attribute to a PlaceHolderElement ?");
        }
        tag.addAttribute(name, value);
        return this;
    }

    /**
     * Add a son to this HtmlElement
     *
     * @param html the htmlNode son to add
     * @return itself
     */
    protected XmlElement add(final XmlNode html) {
        children.add(html);
        return this;
    }

    /**
     * Adds some raw text to this HtmlElement
     *
     * @param text the text to add
     * @return itself
     */
    protected XmlElement addText(final String text) {
        children.add(new HtmlText(text));
        return this;
    }

    /**
     * Return wether this element has at least one child
     *
     * @return <code>true</code> if this element has at least one child,
     *         <code>false</code> otherwise
     */
    public final boolean hasChild() {
        return iterator().hasNext();
    }

    @Override
    public final Iterator<XmlNode> iterator() {
        return children.iterator();
    }

    @Override
    public final void write(final QueryResponseStream txt) {
        if (tag != null) {
            writeTagAndOffspring(txt);
        } else {
            for (final XmlNode html : this) {
                html.write(txt);
            }
        }
    }

    protected void writeTagAndOffspring(final QueryResponseStream txt) {
        if (!hasChild()) {
            txt.writeNewLineChar();
            txt.writeIndentation();
            txt.writeRawText(tag.getSelfClosingTag());
            txt.writeNewLineChar();
            txt.writeIndentation();
        } else {
            txt.indent();
            txt.writeNewLineChar();
            txt.writeIndentation();
            txt.writeRawText(tag.getOpenTag());
            for (final XmlNode html : this) {
                if (html != null) {
                    html.write(txt);
                }
            }
            txt.unindent();
            txt.writeLine(tag.getCloseTag());
            txt.writeIndentation();
        }
    }

}
