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

package com.bloatit.framework.webserver.components.form;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.NotYetImplementedException;

import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;

/**
 * <p>
 * Class to handle Html drop down boxes (aka Html {@code<select>}).
 * </p>
 * <p>
 * Usage is : create the object, then use addText to add a new line
 * </p>
 */
public class HtmlDropDown<T extends DropDownElement> extends HtmlFormField<T> {
    private final Map<T, HtmlBranch> components = new HashMap<T, HtmlBranch>();

    /**
     * Creates a new HtmlDropDown with a given attribute <code>name</code>
     * 
     * @param name
     *            the value of the Html attribute <code>name</code>
     */
    public HtmlDropDown(final String name) {
        super(new HtmlGenericElement("select"), name);
    }

    /**
     * Creates a new HtmlDropDown with a given attribute <code>name</code> and
     * an html {@code label} to explain the usage of the drop down
     * 
     * @param name
     *            the value of the Html attribute <code>name</code>
     * @param label
     *            the text displayed to explain the drop down
     */
    public HtmlDropDown(final String name, final String label) {
        super(new HtmlGenericElement("select"), name, label);
    }

    public HtmlDropDown(final FormFieldData<T> data, final String label) {
        super(new HtmlGenericElement("select"), data.getFieldName(), label);
        setDefaultValue(data);
        addErrorMessages(data.getFieldMessages());
    }

    /**
     * <p>
     * Sets the default value of the drop down box
     * </p>
     * <p>
     * <b>Do not use this method twice</b>
     * </p>
     * 
     * @param value
     *            the element to set as the default value
     */
    @Override
    protected void doSetDefaultValue(final T value) {
        components.get(value).addAttribute("selected", "selected");
    }

    /**
     * Sets the default value based on a string value
     * @throws NotYetImplementedException everytime (not yet implemented)
     */
    @Override
    protected void doSetDefaultValue(String defaultValueAsString) {
        // TODO: I don't even understand...
        throw new NotYetImplementedException("The method doSetDefaultValue(string) is not yet implemented (Tom's fault)");
    }

    /**
     * <p>
     * Adds a new selectable value to the DropDown
     * </p>
     * 
     * @param elem
     *            the value to add
     * @return itself
     */
    public HtmlElement add(final T elem) {
        final HtmlGenericElement opt = new HtmlGenericElement("option");
        opt.addText(elem.getName());
        opt.addAttribute("value", elem.getCode());
        ((HtmlBranch) element).add(opt);

        components.put(elem, ((HtmlBranch) element));
        return this;
    }
}
