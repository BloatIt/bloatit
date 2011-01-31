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

import com.bloatit.framework.webserver.components.form.HtmlSimpleInput.InputType;

public final class HtmlCheckbox extends HtmlFormField<Boolean> {

    public HtmlCheckbox(final String name, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.CHECKBOX_INPUT)), name, position);
    }

    public HtmlCheckbox(final String name, final String label, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.CHECKBOX_INPUT)), name, label, position);
    }

    @Override
    protected void doSetDefaultValue(final Boolean value) {
        if (value.booleanValue()) {
            addAttribute("checked", "checked");
        }
    }
}
