/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.webprocessor.components.form;

import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlNamedNode;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Messages;

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

    private LabelPosition position;

    /**
     * <p>
     * Describes the relative position of the label, compared to the element it
     * describes.
     * </p>
     * <p>
     * Generic usage is : - Checkbox & Radio buttons should used
     * <code>AFTER</code>- All other fields should use <code>BEFORE</code>
     * </p>
     */
    public enum LabelPosition {

        /**
         * <b>BEFORE</b> means the label is positioned before the aforementioned
         * element. Example :
         * <p>
         * {@code<label> ... </label><element />}
         * </p>
         */
        BEFORE,
        /**
         * <b>AFTER</b> means the label is positioned after the aforementioned
         * element. Example :
         * <p>
         * {@code<element /><label> ... </label>}
         * </p>
         */
        AFTER
    }

    protected PlaceHolderElement commentPh = new PlaceHolderElement();
    private final PlaceHolderElement notificationPh = new PlaceHolderElement();
    protected InputBlock inputBlock;
    private final PlaceHolderElement ph = new PlaceHolderElement();
    private HtmlLabel label;
    private final HtmlDiv container = new HtmlDiv();
    private final HtmlDiv input = new HtmlDiv();
    private final RandomString rng = new RandomString(10);

    /**
     * <p>
     * Creates a form field for a given element, and a given name.
     * </p>
     * <p>
     * If a label is added, it will will be positioned BEFORE the element
     * </p>
     * 
     * @param element the element to add
     * @param name the name of the element
     */
    protected HtmlFormField(final InputBlock inputBlock, final String name) {
        this(inputBlock, name, LabelPosition.BEFORE);
    }

    /**
     * <p>
     * Creates a form field for a given element, with a given name and a given
     * label.
     * </p>
     * <p>
     * The Label will be positioned BEFORE the element
     * </p>
     * 
     * @param element the element to add
     * @param name the name of the element
     * @param label the label of the element
     */
    protected HtmlFormField(final InputBlock inputBlock, final String name, final String label) {
        this(inputBlock, name, label, LabelPosition.BEFORE);
    }

    /**
     * <p>
     * Creates a form field for a given element, with a given name and a given
     * label.
     * </p>
     * <p>
     * If a label is added later, it will be added before or after the element,
     * depending on the value of the parameter <code>position</code>
     * <p>
     * 
     * @param element the element to add
     * @param name the name of the element
     * @param position the position of the future label
     */
    protected HtmlFormField(final InputBlock inputBlock, final String name, final LabelPosition position) {
        super();
        this.inputBlock = inputBlock;
        this.position = position;
        this.setName(name);
        init();
    }

    /**
     * <p>
     * Creates a form field for a given element, with a given name and a given
     * label.
     * </p>
     * <p>
     * The label position depends on the value of the parameter
     * <code>position</code>
     * </p>
     * 
     * @param element the element to add
     * @param name the name of the element
     * @param label the label of the element
     * @param position the position of the future label
     */
    protected HtmlFormField(final InputBlock inputBlock, final String name, final String label, final LabelPosition position) {
        super();
        this.inputBlock = inputBlock;
        this.position = position;
        this.setName(name);
        init();
        this.setLabel(label);
    }

    public HtmlFormField(final InputBlock inputBlock, final String name, final HtmlElement label, final LabelPosition position) {
        super();
        this.inputBlock = inputBlock;
        this.position = position;
        this.setName(name);
        init();
        this.setLabel(label);
    }

    /**
     * <p>
     * Add a message indicating to the user that his input is not correct
     * </p>
     * <p>
     * Error message will be added close to the form field, with a position
     * depending on the kind of field. CSS can then be used to render it
     * properly.
     * </p>
     * 
     * @param messages The list of messages to display
     */
    public void addErrorMessages(final Messages messages) {
        final HtmlDiv notifyBlock = new HtmlDiv("notification_error");
        for (final Message message : messages) {
            notifyBlock.add(new HtmlParagraph(Context.tr(message.getMessage())));
        }
        this.notificationPh.add(notifyBlock);
        if (!messages.isEmpty()) {
            inputBlock.getInputElement().setCssClass("has_error");
        }
    }

    /**
     * @return the inputBlock
     */
    protected InputBlock getInputBlock() {
        return inputBlock;
    }

    /**
     * <p>
     * Sets the label for the object
     * </p>
     * <p>
     * <b>CONTRACT :</b> Any class overriding this method have to be careful and
     * not modify any other parameters than redefining the placeholder
     * </p>
     * 
     * @param label the label for the element
     */
    public final void setLabel(final String label) {
        final HtmlDiv labelBlock = new HtmlDiv("label");
        this.label = new HtmlLabel(label);

        labelBlock.add(this.label);
        this.ph.add(labelBlock);
        checkIdLabel();
    }

    /**
     * <p>
     * Sets the label for the object
     * </p>
     * <p>
     * <b>CONTRACT :</b> Any class overriding this method have to be careful and
     * not modify any other parameters than redefining the placeholder
     * </p>
     * 
     * @param label the label for the element
     */
    public final void setLabel(final HtmlElement label) {
        final HtmlDiv labelBlock = new HtmlDiv("label");
        this.label = new HtmlLabel(label);

        labelBlock.add(this.label);
        this.ph.add(labelBlock);
        checkIdLabel();
    }
    
    /**
     * <p>
     * Adds some text that explains the meaning of this
     * <code>HtmlFormField</code>
     * </p>
     * <p>
     * Comment text will be added close to the form field, with a position
     * depending on the kind of field. CSS can then be used to render it
     * properly.
     * </p>
     * 
     * @param comment The text describing the goal of the form field
     */
    public final void setComment(final String comment) {
        setComment(new HtmlText(comment));
    }

    public void setComment(final HtmlNode comment) {
        final HtmlDiv commentBlock = new HtmlDiv("comment");
        commentBlock.add(comment);
        this.commentPh.add(commentBlock);
    }

    @Override
    public final HtmlElement setId(final String id) {
        if (this.label != null) {
            this.label.setFor(id);
        }
        inputBlock.getInputElement().setId(id);
        return this;
    }

    @Override
    public final String getId() {
        return inputBlock.getInputElement().getId();
    }

    @Override
    public final HtmlFormField<T> addAttribute(final String name, final String value) {
        this.inputBlock.getInputElement().addAttribute(name, value);
        return this;
    }

    @Override
    public final void setName(final String name) {
        inputBlock.getInputElement().addAttribute("name", name);
    }

    /**
     * <p>
     * Adds a default value to the object object.
     * </p>
     * The corresponding element must have been added
     * 
     * @param value the Object representing the default value
     */
    public final void setDefaultValue(final String value) {
        if (value != null) {
            this.doSetDefaultStringValue(value);
        }
    }

    public final void setDefaultStringValue(final T value) {
        if (value != null) {
            this.doSetDefaultValue(value);
        }
    }

    public final void turnOffAutoComplete() {
        inputBlock.getInputElement().addAttribute("autocomplete", "off");
    }

    /**
     *
     */
    private void checkIdLabel() {
        if (getId() == null) {
            setId(rng.nextString());
        } else {
            label.setFor(getId());
        }
    }

    /**
     * <p>
     * Method to implement to add a default value to the elements of the class
     * </p>
     * <p>
     * Default value of an element is the value displayed to the user when the
     * page loads, before he even started adding data.
     * </p>
     * 
     * @param value the default value
     */
    protected abstract void doSetDefaultStringValue(String value);

    protected abstract void doSetDefaultValue(T value);

    /**
     * Initializes the placeholder for the label
     */
    private void init() {

        switch (position) {
            case AFTER:
                this.container.add(input);
                this.container.add(ph);
                break;
            case BEFORE:
            default:
                this.container.add(ph);
                this.container.add(input);
                break;
        }
        this.input.setCssClass("input");

        final HtmlDiv inputDiv = new HtmlDiv("input_field");
        inputDiv.add(inputBlock.getContentElement());
        this.input.add(inputDiv);

        this.input.add(commentPh);
        this.input.add(notificationPh);

        add(container);
        container.setCssClass("field");
    }

    /**
     * <p>
     * The inputBlock interface permit to create complex form structure with a
     * tree of html element and not only a input tag.
     * </p>
     * <p>
     * Many methods of HtmlFormField are overriding the HtmlElement ones to
     * apply the changes to the input field and not to the root element. If the
     * input element is a tree, the form field doesn't know where to add the
     * attribute. This class permit to give two reference, one to the input
     * element and one on the root sub-element.
     * </p>
     */
    public abstract static class InputBlock {

        /**
         * Return the input tag element
         */
        public abstract HtmlElement getInputElement();

        /**
         * return the element to add in the tree
         */
        public abstract HtmlElement getContentElement();

        protected static InputBlock create(final HtmlElement element) {
            return new InputBlock() {

                @Override
                public HtmlElement getInputElement() {
                    return element;
                }

                @Override
                public HtmlElement getContentElement() {
                    return element;
                }
            };
        }

    }

}
