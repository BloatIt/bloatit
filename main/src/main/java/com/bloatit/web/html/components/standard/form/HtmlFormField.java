/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.html.components.standard.form;

import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlLeaf;
import com.bloatit.web.html.components.HtmlNamedNode;
import com.bloatit.web.html.components.PlaceHolderElement;
import com.bloatit.web.html.components.standard.HtmlParagraph;


/**
 * <p>
 * Basic class to describe elements that can be added to a form
 * </p>
 * <p>
 * All elements inheriting from this class can have an additional label and a
 * default value
 * </p>
 */
public abstract class HtmlFormField<T extends Object> extends HtmlLeaf implements HtmlNamedNode {

    protected PlaceHolderElement ph = new PlaceHolderElement();
    protected HtmlLabel label;
    protected HtmlParagraph paragraph = new HtmlParagraph();
    protected HtmlElement element;
    private String name;

    public HtmlFormField(final HtmlElement element, final String name) {
        super();
        this.paragraph.add(ph);
        this.element = element;
        this.paragraph.add(element);
        this.add(paragraph);
        this.setName(name);
    }

    public HtmlFormField(final HtmlElement element, final String name, final String label) {
        super();
        this.paragraph.add(ph);
        this.element = element;
        this.paragraph.add(element);
        this.setLabel(label);
        this.setName(name);
        this.add(paragraph);
    }

    /**
     * <p>
     * Sets the label for the object
     * </p>
     * <p>
     * <b>CONTRACT :</b> Any class overriding this method have to be careful and
     * not modify any other parameters than predefininglaceholder
     * </p>
     * 
     * @param label the label for the element
     */
    public void setLabel(final String label) {
        this.label = new HtmlLabel(label);
        this.ph.add(this.label);

        if (name != null) {
            this.label.setFor(name);
        }
        // BIG TODO
    }

    @Override
    public HtmlFormField<T> addAttribute(final String name, final String value) {
        this.element.addAttribute(name, value);
        return this;
    }

    @Override
    public final void setName(final String name) {
        this.name = name;
        element.addAttribute("name", name);

        if (label != null) {
            label.setFor(name);
        }
    }

    /**
     * <p>
     * Adds a default value to the object object.
     * </p>
     * <p>
     * The valued added will be obtained using toString on <i>value</i>. If
     * <i>value</i> is null, no defaultValue is added.
     * </p>
     * 
     * @param value the Object representing the default value
     */
    public void setDefaultValue(final T value) {
        if (value != null) {
            this.doSetDefaultValue(value);
        }
    }

    /**
     * <p>
     * Method to implement to add a default value to the elements of the class
     * </p>
     * 
     * @param value the value
     */
    protected abstract void doSetDefaultValue(T value);
}