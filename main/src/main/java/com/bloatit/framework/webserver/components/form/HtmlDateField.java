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

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.form.HtmlFormField.InputBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlTagText;

/**
 * <p>
 * Class used to create input fields used to input date
 * </p>
 */
public final class HtmlDateField extends HtmlFormField<DateLocale> {

    public HtmlDateField(final String name) {
        super(InputBlock.create(new HtmlSimpleInput("text")), name);
    }

    public HtmlDateField(final String name, final String label) {
        super(new DateInputBlock(), name, label);
    }

    @Override
    protected void doSetDefaultValue(final DateLocale value) {
        addAttribute("value", value.toString(DateLocale.FormatStyle.SHORT));
    }

    @Override
    protected void doSetDefaultStringValue(final String defaultValueAsString) {
        addAttribute("value", defaultValueAsString);
    }
}

class DateInputBlock extends InputBlock {
    PlaceHolderElement container;
    HtmlSimpleInput input;
    HtmlGenericElement script;

    public DateInputBlock() {
        container = new PlaceHolderElement();
        input = new HtmlSimpleInput("text");
        input.setId("datepicker");
        script = new HtmlGenericElement("script");
        script.add(new HtmlTagText("$(function() {\n" + "$( \"#datepicker\" ).datepicker(); \n" + "});"));
        container.add(script);
        container.add(input);
    }

    @Override
    public HtmlElement getInputElement() {
        return input;
    }

    @Override
    public HtmlElement getContentElement() {
        return container;
    }
}
