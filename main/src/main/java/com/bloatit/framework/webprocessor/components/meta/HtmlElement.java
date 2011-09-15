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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

public abstract class HtmlElement extends HtmlNode {

    private final List<HtmlNode> children = new ArrayList<HtmlNode>();

    protected HtmlElement(final String tag) {
        super(tag);
    }

    public HtmlElement() {
        super((HtmlTag) null);
    }

    protected HtmlElement(final HtmlElement element) {
        super(element.tag);
        for (final HtmlNode child : element.children) {
            this.children.add(child);
        }
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
    public HtmlElement addAttribute(final String name, final String value) {
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
    protected HtmlElement add(final HtmlNode html) {
        children.add(html);
        return this;
    }

    /**
     * Adds some raw text to this HtmlElement
     * 
     * @param text the text to add
     * @return itself
     */
    protected HtmlElement addText(final String text) {
        children.add(new HtmlText(text));
        return this;
    }

    @Override
    public final Iterator<HtmlNode> iterator() {
        return children.iterator();
    }

}
