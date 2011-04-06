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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;

/**
 * Class to create Html drop down ({@code<select>} tag)
 */
public class HtmlDropDown extends HtmlStringFormField {

    private final Map<String, HtmlGenericElement> elements = new HashMap<String, HtmlGenericElement>();

    public HtmlDropDown(final String name) {
        super(InputBlock.create(new HtmlGenericElement("select")), name);
    }

    public HtmlDropDown(final String name, final String label) {
        super(InputBlock.create(new HtmlGenericElement("select")), name, label);
    }

    public void addDropDownElement(final String value, final String displayName) {
        final HtmlGenericElement opt = new HtmlGenericElement("option");
        opt.addText(displayName);
        opt.addAttribute("value", value);
        ((HtmlBranch) inputBlock.getInputElement()).add(opt);
        elements.put(value, opt);
    }

    /**
     * Adds elements based on an enum
     * 
     * @param <T> the type of the elements of the set
     * @param elements the enum set
     */
    public <T extends Enum<T> & Displayable> void addDropDownElements(final EnumSet<T> elements) {
        for (final T enumValue : elements) {
            addDropDownElement(enumValue.name(), enumValue.getDisplayName());
        }
    }

    /**
     * Sets the default value of the drop down menu
     * <p>
     * The default value is set based on the <i>value</i> field of the
     * {@link #addDropDownElement(String, String)} method (the code which is not
     * visible from the user).
     * </p>
     * 
     * @param value the code of the default element
     */
    @Override
    protected void doSetDefaultValue(final String value) {
        final HtmlGenericElement checkedElement = elements.get(value);
        if (checkedElement != null) {
            checkedElement.addAttribute("selected", "selected");
        }
    }
}
