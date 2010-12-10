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

package test.html.components.basicComponents.formComponents;

import test.html.HtmlElement;
import test.html.components.HtmlNamedNode;
import test.html.components.PlaceHolderElement;
import test.html.components.basicComponents.HtmlParagraph;

/**
 * <p>Basic class to describe elements that can be added to a form</p>
 * <p>All elements inheriting from this class can have an additional label and
 * a default value</p>
 */
public abstract class HtmlFormField<T extends Object> extends HtmlElement implements HtmlNamedNode{

    protected PlaceHolderElement ph = new PlaceHolderElement();
    protected HtmlLabel label;
    protected HtmlParagraph paragraph = new HtmlParagraph();
    protected HtmlElement element;

    public HtmlFormField(HtmlElement element, String name){
        super();
        this.paragraph.add(ph);
        this.element = element;
        this.setName(name);
    }

    public HtmlFormField(HtmlElement element, String name, String label){
        super();
        this.paragraph.add(ph);
        this.element = element;
        this.setLabel(label);
        this.setName(name);
    }

    /**
     * <p>Sets the label for the object</p>
     * <p><b>CONTRACT :</b> Any class overriding this method have to be careful
     * and not modify any other parameters than predefininglaceholder</p>
     * @param label the label for the element
     */
    public void setLabel(String label){
        this.label = new HtmlLabel(label);
        this.ph.add(this.label);
    }

    @Override
    public HtmlFormField addAttribute(String name, String value){
        this.element.addAttribute(name, value);
        return this;
    }

    @Override
    public final void setName(String name) {
        this.element.addAttribute("name", name);
        this.label.setFor(name);
    }

    /**
     * <p>Adds a default value to the object object.</p>
     * <p>The valued added will be obtained using toString on <i>value</i>.
     * If <i>value</i> is null, no defaultValue is added.</p>
     * 
     * @param value the Object representing the default value
     */
    public void setDefaultValue(T value){
        if(value != null ){
            this.doSetDefaultValue(value);
        }
    }

    /**
     * <p>Method to implement to add a default value to the elements of the
     * class</p>
     * @param value the value
     */
    protected abstract void doSetDefaultValue(T value);
}