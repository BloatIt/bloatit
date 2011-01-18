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

import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlGenericElement;

/**
 * <p>
 * Class to handle Html drop down boxes (aka Html select).
 * </p>
 * <p>
 * Usage is : create the object, then use addText to add a new line
 * </p>
 */
public class HtmlDropDown<T extends DropDownElement> extends HtmlFormField<T> {
    private final Map<T, HtmlBranch> components = new HashMap<T, HtmlBranch>();

    public HtmlDropDown(final String name) {
        super(new HtmlGenericElement("select"), name);
    }

    public HtmlDropDown(final String name, final String label) {
        super(new HtmlGenericElement("select"), name, label);
    }

    /**
     * <p>
     * Sets the default value of the drop down box
     * </p>
     * <p>
     * Do not use this method twice
     * </p>
     *
     * @param value the index of the element, 0 being the first element inserted with
     *        addText
     */
    @Override
    protected void doSetDefaultValue(final T value) {
        components.get(value).addAttribute("selected", "selected");
    }

    public HtmlElement add(T elem) {
        final HtmlGenericElement opt = new HtmlGenericElement("option");
        opt.addText(elem.getName());
        opt.addAttribute("value", elem.getCode());
        ((HtmlBranch)element).add(opt);

        components.put(elem, ((HtmlBranch)element));
        return this;
    }
}
