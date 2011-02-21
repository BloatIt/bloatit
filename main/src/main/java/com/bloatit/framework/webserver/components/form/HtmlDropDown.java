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

public class HtmlDropDown extends HtmlFormField<String> {

    private String checked = null;
    private final Map<String, HtmlGenericElement> elements = new HashMap<String, HtmlGenericElement>();

    public HtmlDropDown(final FormFieldData<?> data) {
        this(data.getFieldName());
        setDefaultOnConstruction(data);
    }

    public HtmlDropDown(final FormFieldData<?> data, final String label) {
        this(data.getFieldName(), label);
        setDefaultOnConstruction(data);
    }

    private void setDefaultOnConstruction(final FormFieldData<?> data) {
        checked = data.getSuggestedValue();
    }

    public HtmlDropDown(final String name) {
        super(new HtmlGenericElement("select"), name);
    }

    public HtmlDropDown(final String name, final String label) {
        super(new HtmlGenericElement("select"), name, label);
    }

    public void addDropDownElement(final String value, final String displayName) {
        final HtmlGenericElement opt = new HtmlGenericElement("option");
        opt.addText(displayName);
        opt.addAttribute("value", value);

        if (value.equals(checked)) {
            opt.addAttribute("selected", "selected");
        }
        ((HtmlBranch) element).add(opt);
        elements.put(value, opt);
    }

    public <T extends Enum<T> & Displayable> void addDropDownElements(final EnumSet<T> elements) {
        for (final T enumValue : elements) {
            addDropDownElement(enumValue.name(), enumValue.getDisplayName());
        }
    }

    @Override
    protected void doSetDefaultValue(final String value) {
        final HtmlGenericElement checkedElement = elements.get(value);
        if (checkedElement != null) {
            checkedElement.addAttribute("checked", "checked");
        }
    }
}
