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

import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.meta.HtmlLeaf;

public final class HtmlRadioButtonGroup extends HtmlLeaf {

    private final LabelPosition position;
    private final String name;

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
        return button;
    }

    public static interface Displayable {
        String getDisplayName();
    }

    public <T extends Enum<T> & Displayable> void addRadioButton(EnumSet<T> buttons) {
        for (T enumValue : buttons) {
            final HtmlRadioButton button = new HtmlRadioButton(name, enumValue.name(), enumValue.getDisplayName(), position);
            add(button);
        }
    }
}
