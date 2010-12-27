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

public class HtmlRadioButton extends HtmlFormField<Boolean> {

    public HtmlRadioButton(final String name, final String value, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.RADIO), name, position);
        addAttribute("value", value);
    }

    public HtmlRadioButton(final String name, final String value, final String label, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.RADIO), name, label, position);
        addAttribute("value", value);
    }

    @Override
    protected void doSetDefaultValue(final Boolean value) {
        if (value.booleanValue()) {
            addAttribute("checked", "checked");
        }
    }
}