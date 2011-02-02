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

/**
 * A class used to create input fields of password type The result will be :
 * 
 * <pre>
 * <p>
 *      <label for="...">plop</label>
 *      <input id="..." type="password" class="cssClass" ...>default value</input>
 * </p>
 * </pre>
 */
public final class HtmlPasswordField extends HtmlFormField<String> {

    public HtmlPasswordField(final String name) {
        super(new HtmlSimpleInput("password"), name);
    }

    public HtmlPasswordField(final String name, final String label) {
        super(new HtmlSimpleInput("password"), name, label);
    }

    @Override
    protected void doSetDefaultValue(final String value) {
        this.element.addAttribute("value", value);
    }
}