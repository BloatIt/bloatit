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
 * along with BloatIt. If not, see <http://www    enum LabelPosition{
        BEFORE, AFTER
    }.gnu.org/licenses/>.
 */
package com.bloatit.web.html.components.standard.form;

import com.bloatit.web.html.HtmlLeaf;
import com.bloatit.web.html.components.standard.form.HtmlFormField.LabelPosition;

/**
 *
 */
public class CheckBoxGroup extends HtmlLeaf {

    private final LabelPosition position;

    public CheckBoxGroup() {
        super();
        this.position = LabelPosition.AFTER;
    }

    public CheckBoxGroup(final LabelPosition position) {
        super();
        this.position = position;
    }

    public HtmlCheckbox addCheckBox(final String name, final String label) {
        final HtmlCheckbox box = new HtmlCheckbox(name, label, position);
        add(box);
        return box;
    }
}
