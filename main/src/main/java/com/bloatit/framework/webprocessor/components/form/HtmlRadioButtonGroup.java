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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;

public final class HtmlRadioButtonGroup extends HtmlLeaf {

    private final LabelPosition position;
    private final String name;
    private final Map<String, HtmlRadioButton> buttons = new HashMap<String, HtmlRadioButton>();

    public HtmlRadioButtonGroup(final String name) {
        super();
        this.name = name;
        this.position = LabelPosition.AFTER;
    }

    public HtmlRadioButtonGroup(final String name, final LabelPosition position) {
        super();
        this.name = name;
        this.position = position;
    }

    public HtmlRadioButton addRadioButton(final String value, final String label) {
        final HtmlRadioButton button = new HtmlRadioButton(name, value, label, position);
        add(button);
        buttons.put(value, button);
        return button;
    }

    public <T extends Enum<T> & Displayable> void addRadioButton(final EnumSet<T> buttons) {
        for (final T enumValue : buttons) {
            addRadioButton(enumValue.name(), enumValue.getDisplayName());
        }
    }

    public void setDefaultValue(final String value) {
        final HtmlRadioButton checkedElement = buttons.get(value);
        if (checkedElement != null) {
            checkedElement.addAttribute("checked", "selected");
        }
    }
}
