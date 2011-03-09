package com.bloatit.framework.webserver.components.meta;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.webserver.components.writers.QueryResponseStream;

/**
 * <p>
 * An <code>HtmlElement</code> represents any HtmlNode which is not raw text,
 * hence it should be the mother class of all HtmlTags that are created.
 * </p>
 */
public abstract class HtmlElement extends XmlElement {
    public HtmlElement(final String tag) {
        super(tag);
    }

    public HtmlElement() {
        super();
    }

    public HtmlElement(HtmlElement element) {
        super(element);
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

    /**
     * <p>
     * Indicates whether the tag can be self closed or not
     * </p>
     * <p>
     * All inheriting classes
     */
    public abstract boolean selfClosable();

    /**
     * This method should be overriden by any components needing some special
     * css files.
     *
     * @return the list of custom Css files needed by this component or null if
     *         no special js is needed
     */
    @Override
    protected List<String> getCustomCss() {
        return new ArrayList<String>();
    }

    /**
     * This method should be overriden by any components needing some special
     * javascript files.
     *
     * @return the list of custom js file needed by this component or null if no
     *         special js is needed
     */
    @Override
    protected List<String> getCustomJs() {
        return null;
    }


    @Override
    protected final void writeTagAndOffspring(final QueryResponseStream txt) {
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
