package com.bloatit.web.components;

import java.util.Map.Entry;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.webserver.components.form.DropDownElement;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;

/**
 * A simple component that proposes the users a drop down menu allowing him to
 * select a language
 */
public class LanguageSelector extends HtmlDropDown<LanguageDescriptor> {

    public LanguageSelector(String name, String label) {
        super(name, label);
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            add(langEntry.getValue());
        }
    }

    public LanguageSelector(FormFieldData<?> languageFormFieldData, String label) {
        super(languageFormFieldData, label);
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            add(langEntry.getValue());
        }
        doSetDefaultValue(languageFormFieldData.getFieldDefaultValueAsString());
    }
}