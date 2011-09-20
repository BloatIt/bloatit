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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.InputBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNonEscapedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.web.WebConfiguration;

/**
 * <p>
 * Class used to create input fields used to input date
 * </p>
 */
public final class HtmlDateField extends HtmlFormField<DateLocale> {
    private static final String DEFAULT_LOCALE = "en";
    private final String locale;

    public HtmlDateField(final String name, final Locale userLocale) {
        super(new DateInputBlock(userLocale.getLanguage(), "datepicker_" + new RandomString(10).nextString()), name);
        locale = userLocale.getLanguage();
    }

    public HtmlDateField(final String name, final String label, final Locale userLocale) {
        super(new DateInputBlock(userLocale.getLanguage(), "datepicker_" + new RandomString(10).nextString()), name, label);
        locale = userLocale.getLanguage();
    }

    @Override
    protected void doSetDefaultValue(final DateLocale value) {
        addAttribute("value", value.toString(DateLocale.FormatStyle.SHORT));
    }

    @Override
    protected void doSetDefaultStringValue(final String defaultValueAsString) {
        addAttribute("value", defaultValueAsString);
    }

    @Override
    protected List<String> getCustomCss() {
        final ArrayList<String> custom = new ArrayList<String>();
        custom.add(WebConfiguration.getCssDatePicker());
        return custom;
    }

    @Override
    protected List<String> getCustomJs() {
        final ArrayList<String> customJsList = new ArrayList<String>();
        customJsList.add(FrameworkConfiguration.getJsJquery());
        customJsList.add(FrameworkConfiguration.getJsJqueryUi());
        if (!locale.equals(DEFAULT_LOCALE)) {
            customJsList.add(FrameworkConfiguration.getJsDatePicker(locale));
        }
        return customJsList;
    }
}

class DateInputBlock extends InputBlock {
    private final PlaceHolderElement container;
    private final HtmlSimpleInput input;
    private final HtmlScript script;
    private PlaceHolderElement options;
    private boolean firstOption = true;

    public DateInputBlock(final String languageCode, final String id) {
        container = new PlaceHolderElement();
        input = new HtmlSimpleInput("text");
        input.addAttribute("autocomplete", "off");
        input.setId(id);
        script = new HtmlScript();
        script.append("$.datepicker.setDefaults( $.datepicker.regional[ '" + languageCode + "' ] ); \n");
        script.append("$(function() {\n" + //
                "    $( \"#" + id + "\" ).datepicker({\"minDate\": 0, \"dateFormat\": \"yy-mm-dd\" }); \n " + //
                "});");
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
