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

package com.bloatit.framework.webserver.components.form;

/**
 * <p>
 * A class used to create input fields of password type
 * </p>
 * <p>
 * The result will be :
 *
 * <pre>
 * {@code
 * <label for="...">plop</label>
 * <input id="..." type="password" class="cssClass" ...>default value</input>}
 * </pre>
 *
 * </p>
 */
public final class HtmlPasswordField extends HtmlFormField<String> {

    /**
     * <p>
     * Creates a form field for a given element, and a given name.
     * </p>
     * <p>
     * If a label is added, it will will be positioned BEFORE the element
     * </p>
     *
     * @param name the name of the element
     */
    public HtmlPasswordField(final String name) {
        super(new HtmlSimpleInput("password"), name);
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
     * @param name the name of the element
     * @param label the label of the element
     */
    public HtmlPasswordField(final String name, final String label) {
        super(new HtmlSimpleInput("password"), name, label);
    }

    public HtmlPasswordField(final FormFieldData<String> data, final String label) {
        super(new HtmlSimpleInput("password"), data.getFieldName(), label);
        setDefaultValue(data);
        addErrorMessages(data.getErrorMessages());
    }

    @Override
    protected void doSetDefaultValue(final String value) {
        this.element.addAttribute("value", value);
    }
}
