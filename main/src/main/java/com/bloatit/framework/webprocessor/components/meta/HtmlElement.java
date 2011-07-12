//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.components.meta;

import java.util.List;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.components.writers.QueryResponseStream;

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

    protected HtmlElement(final HtmlElement element) {
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
     * This method should be overridden by any components needing some special
     * css files.
     * 
     * @return the list of custom css files needed by this component or null if
     *         no special css is needed
     */
    @Override
    protected List<String> getCustomCss() {
        return null;
    }

    /**
     * This method should be overridden by any components needing some special
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
