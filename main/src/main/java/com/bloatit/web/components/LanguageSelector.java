package com.bloatit.web.components;

import java.util.Map.Entry;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;

/**
 * A simple component that proposes the users a drop down menu allowing him to select a
 * language
 */
public class LanguageSelector extends HtmlDropDown {

    public LanguageSelector(final String name, final String label) {
        super(name, label);
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            addDropDownElement(langEntry.getValue().getCode(), langEntry.getValue().getName());
        }
    }

    public LanguageSelector(final FormFieldData<?> languageFormFieldData, final String label) {
        super(languageFormFieldData, label);
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            addDropDownElement(langEntry.getValue().getCode(), langEntry.getValue().getName());
        }
        doSetDefaultValue(languageFormFieldData.getFieldDefaultValueAsString());
    }
}
