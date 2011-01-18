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
 * This version only handles simple string. To handle complex elements, use HtmlDropDown</p>
 * <p>
 * Usage is : create the object, then use add to add a new line
 * </p>
 */
public class HtmlSimpleDropDown extends HtmlFormField<String> {
    private Map<String, HtmlBranch> components = new HashMap<String, HtmlBranch>();

    public HtmlSimpleDropDown(final String name) {
        super(new HtmlGenericElement("select"), name);
    }

    public HtmlSimpleDropDown(final String name, final String label) {
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
     * @param elem
     *            is the name of the element to select
     */
    @Override
    protected void doSetDefaultValue(final String elem) {
        components.get(elem).addAttribute("selected", "selected");
    }

    /**
     * Adds a new line to the drop down
     * 
     * @param displayedName
     *            the text displayed for the element
     * @param value
     *            the real value (not visible) of the element
     * @return itself
     */
    public HtmlElement add(String displayedName, String value) {
        final HtmlGenericElement opt = new HtmlGenericElement("option");
        opt.addText(displayedName);
        opt.addAttribute("value", value);
        ((HtmlBranch) element).add(opt);

        components.put(displayedName, ((HtmlBranch) element));
        return this;
    }
}
