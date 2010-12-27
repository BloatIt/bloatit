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

import com.bloatit.web.html.HtmlLeaf;

/**
 * Meta class used to create various input fields
 */
public class HtmlSimpleInput extends HtmlLeaf {

    protected final static String TEXT = "text";
    protected final static String PASSWORD = "password";
    protected final static String FILE = "file";
    protected final static String CHECKBOX = "checkbox";
    protected final static String RADIO = "radio";
    protected final static String BUTTON = "button";
    protected final static String SUBMIT = "submit";
    protected final static String RESET = "reset";
    protected final static String HIDDEN = "hidden";

    protected HtmlSimpleInput(final String type) {
        super("input");
        addAttribute("type", type);
    }

    protected HtmlSimpleInput setName(final String name) {
        addAttribute("name", name).addAttribute("id", name);
        return this;
    }
}
