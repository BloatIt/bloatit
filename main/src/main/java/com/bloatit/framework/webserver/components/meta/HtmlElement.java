package com.bloatit.framework.webserver.components.meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.webserver.components.writers.HtmlStream;

/**
 * <p>An 
 */
public abstract class HtmlElement extends HtmlNode {

    private final List<HtmlNode> children = new ArrayList<HtmlNode>();
    private final HtmlTag tag;

    public HtmlElement(final String tag) {
        super();
        this.tag = new HtmlTag(tag);
    }

    public HtmlElement() {
        super();
        this.tag = null;
    }

    public HtmlElement addAttribute(final String name, final String value) {
        if (name.equals("id")) {
            setId(value);
        } else {
            tag.addAttribute(name, value);
        }
        return this;
    }

    public HtmlElement add(final HtmlNode html) {
        children.add(html);
        return this;
    }

    protected HtmlElement addText(final String text) {
        children.add(new HtmlText(text));
        return this;
    }

    /**
     * <p>
     * Sets the id of the html element :
     * 
     * <pre>
     * <element id="..." />
     * </pre>
     * 
     * </p>
     * <p>
     * Shortcut to element.addAttribute("id",value)
     * </p>
     * 
     * @param id the value of the id
     * @return the element
     */
    public HtmlElement setId(final String id) {
        tag.addId(id);
        return this;
    }

    /**
     * Finds the id of the element
     * 
     * <pre>
     * <element id="value" />
     * </pre>
     * 
     * @return The value contained in the attribute id of the element
     */
    public String getId() {
        if (tag != null) {
            return this.tag.getId();
        }
        return null;
    }

    public boolean hasChild() {
        return iterator().hasNext();
    }

    /**
     * Sets the css class of the element
     * <p>
     * Shortcut for element.addattribute("class",cssClass)
     * </p>
     * 
     * @param cssClass
     * @return
     */
    public HtmlElement setCssClass(final String cssClass) {
        addAttribute("class", cssClass);
        return this;
    }

    @Override
    public Iterator<HtmlNode> iterator() {
        return children.iterator();
    }

    @Override
    public final void write(final HtmlStream txt) {
        if (tag != null) {
            writeTagAndOffspring(txt);
        } else {
            for (final HtmlNode html : this) {
                html.write(txt);
            }
        }
    }

    private void writeTagAndOffspring(final HtmlStream txt) {
        if (selfClosable() && !hasChild()) {
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
            for (final HtmlNode html : this) {
                if (html != null) {
                    html.write(txt);
                }
            }
            txt.unindent();
            txt.writeLine(tag.getCloseTag());
            txt.writeIndentation();
        }
    }

    /**
     * Indicates whether the tag is self closed or not
     */
    public abstract boolean selfClosable();
}
