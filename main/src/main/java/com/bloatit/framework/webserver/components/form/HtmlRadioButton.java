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

public final class HtmlRadioButton extends HtmlFormField<Boolean> {

    public HtmlRadioButton(final String name, final String value, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.RADIO_INPUT)), name, position);
        addAttribute("value", value);
    }

    public HtmlRadioButton(final String name, final String value, final String label, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.RADIO_INPUT)), name, label, position);
        addAttribute("value", value);
    }

    public HtmlRadioButton(final FormFieldData<Boolean> data, final String label, final LabelPosition position) {
        super(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.RADIO_INPUT)), data.getFieldName(), label, position);
        setDefaultValue(data);
        addErrorMessages(data.getFieldMessages());
    }

    @Override
    protected void doSetDefaultValue(final Boolean value) {
        if (value.booleanValue()) {
            addAttribute("checked", "checked");
        }
    }

    @Override
    protected void doSetDefaultValue(String defaultValueAsString) {
        if (Boolean.parseBoolean(defaultValueAsString)) {
            addAttribute("checked", "checked");
        }
    }
}