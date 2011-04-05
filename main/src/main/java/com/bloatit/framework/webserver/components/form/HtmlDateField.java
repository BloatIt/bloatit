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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.form.HtmlFormField.InputBlock;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlText;
import com.bloatit.web.WebConfiguration;

/**
 * <p>
 * Class used to create input fields used to input date
 * </p>
 */
public final class HtmlDateField extends HtmlFormField<DateLocale> {
    private static final String DEFAULT_LOCALE = "en";
    private final String locale;

    public HtmlDateField(final String name, Locale userLocale) {
        super(new DateInputBlock(userLocale.getLanguage()), name);
        locale = userLocale.getLanguage();
    }

    public HtmlDateField(final String name, final String label, Locale userLocale) {
        super(new DateInputBlock(userLocale.getLanguage()), name, label);
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

    /**
     * <p>
     * Adds an option to the date picker
     * </p>
     * <p>
     * Example of options :
     * 
     * <pre>
     * myDateField.addDatePickerOption(&quot;minDate&quot;, &quot;0&quot;); // Sets the minimum date to
     * // today
     * myDateField.addDatePickerOption(&quot;formatDate&quot;, &quot;yy-mm-dd&quot;); // Sets the date
     * // format to
     * // 2011-03-09
     * </pre>
     * 
     * </p>
     * 
     * @param name the name of the option
     * @param value the value of the option
     */
    public void addDatePickerOption(String name, String value) {
        ((DateInputBlock) getInputBlock()).addDatePickerOption(name, value);
    }

    @Override
    protected List<String> getCustomCss() {
        ArrayList<String> custom = new ArrayList<String>();
        custom.add(WebConfiguration.getCssDatePicker());
        return custom;
    }

    @Override
    protected List<String> getCustomJs() {
        ArrayList<String> customJsList = new ArrayList<String>();
        customJsList.add(WebConfiguration.getJsJquery());
        customJsList.add(WebConfiguration.getJsJqueryUi());
        if (!locale.equals(DEFAULT_LOCALE)) {
            customJsList.add(WebConfiguration.getJsDatePicker(locale));
        }
        return customJsList;
    }
}

class DateInputBlock extends InputBlock {
    private final PlaceHolderElement container;
    private final HtmlSimpleInput input;
    private final HtmlGenericElement script;
    private PlaceHolderElement options;
    private boolean firstOption = true;

    public DateInputBlock(String languageCode) {
        container = new PlaceHolderElement();
        input = new HtmlSimpleInput("text");
        input.addAttribute("autocomplete", "off");
        input.setId("datepicker");
        script = new HtmlGenericElement("script");
        script.add(new XmlText("$.datepicker.setDefaults( $.datepicker.regional[ '" + languageCode + "' ] ); \n"));
        script.add(new XmlText("$(function() {\n" + //
                "    $( \"#datepicker\" ).datepicker({ "));

        script.add(options = new PlaceHolderElement());

        script.add(new XmlText(" }); \n " + //
                "});"));
        container.add(script);
        container.add(input);

        addDatePickerOption("minDate", "0");
        addDatePickerOption("dateFormat", "yy-mm-dd");
    }

    @Override
    public HtmlElement getInputElement() {
        return input;
    }

    @Override
    public HtmlElement getContentElement() {
        return container;
    }

    public void addDatePickerOption(String optionName, String optionValue) {
        String option = "";
        if (!firstOption) {
            option += ", ";
        } else {
            firstOption = false;
        }

        option += "\"" + optionName + "\" : \"" + optionValue + "\"";
        options.add(new XmlText(option));
    }
}
