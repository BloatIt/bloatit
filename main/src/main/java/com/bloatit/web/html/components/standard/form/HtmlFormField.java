/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html.components.standard.form;

import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlLeaf;
import com.bloatit.web.html.components.HtmlNamedNode;
import com.bloatit.web.html.components.PlaceHolderElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.utils.RandomString;

/**
 * <p>
 * Basic class to describe elements that can be added to a form
 * </p>
 * <p>
 * All elements inheriting from this class can have an additional label and a default
 * value
 * </p>
 */
public abstract class HtmlFormField<T extends Object> extends HtmlLeaf implements HtmlNamedNode {

    private LabelPosition position;

    /**
     * Describes the relative position of the label, compared to the element it describes.
     * Generic usage is : - Checkbox & Radio buttons should used AFTER - All other fields
     * should use BEFORE
     */
    public enum LabelPosition {

        /**
         * <b>BEFORE</b> means the label is positionned before the aformentionned element.
         * Example :
         * <p>
         * <label> ... </label><element />
         * </p>
         */
        BEFORE,
        /**
         * <b>AFTER</b> means the label is positionned after the aformentionned element.
         * Example :
         * <p>
         * <element /><label> ... </label>
         * </p>
         */
        AFTER
    }

    protected PlaceHolderElement ph = new PlaceHolderElement();
    protected HtmlLabel label;
    protected HtmlDiv container = new HtmlDiv();
    protected HtmlElement element;
    private final RandomString rng = new RandomString(10);

    /**
     * Creates a form field for a given element, with a given name. If a label is added,
     * it will will be positionned BEFORE the element
     * 
     * @param element the element to add
     * @param name the name of the element
     */
    protected HtmlFormField(final HtmlElement element, final String name) {
        this(element, name, LabelPosition.BEFORE);
    }

    /**
     * Creates a form field for a given element, with a given name and a given label The
     * Label will be positionned BEFORE the element
     * 
     * @param element the element to add
     * @param name the name of the element
     * @param label the label of the element
     */
    protected HtmlFormField(final HtmlElement element, final String name, final String label) {
        this(element, name, label, LabelPosition.BEFORE);
    }

    /**
     * Creates a form field for a given element, with a given name and a given label If a
     * label is added later, it will be added before or after the element, depending on
     * the value of the parameter <i>position</i>
     * 
     * @param element the element to add
     * @param name the name of the element
     * @param position the position of the future label
     */
    protected HtmlFormField(final HtmlElement element, final String name, final LabelPosition position) {
        super();
        this.element = element;
        this.position = position;
        this.setName(name);
        init();
    }

    /**
     * Creates a form field for a given element, with a given name and a given label The
     * label position depends on the value of the parameter <i>position</i>
     * 
     * @param element the element to add
     * @param name the name of the element
     * @param label the label of the element
     * @param position the position of the future label
     */
    protected HtmlFormField(final HtmlElement element, final String name, final String label, final LabelPosition position) {
        super();
        this.element = element;
        this.position = position;
        this.setName(name);
        init();
        this.setLabel(label);
    }

    /**
     * <p>
     * Sets the label for the object
     * </p>
     * <p>
     * <b>CONTRACT :</b> Any class overriding this method have to be careful and not
     * modify any other parameters than redefining the placeholder
     * </p>
     * 
     * @param label the label for the element
     */
    public void setLabel(final String label) {
        this.label = new HtmlLabel(label);
        this.ph.add(this.label);

        checkIdLabel();
    }

    protected void checkIdLabel() {
        if (getId() == null) {
            setId(rng.nextString());
        } else {
            label.setFor(getId());
        }
    }

    @Override
    public HtmlElement setId(final String id) {
        if (this.label != null) {
            this.label.setFor(id);
        }
        element.setId(id);
        return this;
    }

    @Override
    public String getId() {
        return element.getId();
    }

    @Override
    public HtmlFormField<T> addAttribute(final String name, final String value) {
        this.element.addAttribute(name, value);
        return this;
    }

    @Override
    public final void setName(final String name) {
        element.addAttribute("name", name);
    }

    /**
     * <p>
     * Adds a default value to the object object.
     * </p>
     * <p>
     * The valued added will be obtained using toString on <i>value</i>. If <i>value</i>
     * is null, no defaultValue is added.
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
     * Initializes the placeholder for the label
     */
    private void init() {
    	
    	
        switch (position) {
        case AFTER:
            this.container.add(element);
            this.container.add(ph);
            break;
        case BEFORE:
        default:
            this.container.add(ph);
            this.container.add(element);
            break;
        }
        add(container);
        container.setCssClass("field");
    	
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
