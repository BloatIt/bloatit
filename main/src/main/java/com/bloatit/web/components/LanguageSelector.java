package com.bloatit.web.components;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;

/**
 * A simple component that proposes the users a drop down menu allowing him to
 * select a language
 */
public class LanguageSelector extends HtmlDropDown {
    private static final String DEFAULT_LANG = "en";
    private Set<String> codes = new HashSet<String>();

    public LanguageSelector(final String name, final String label) {
        super(name, label);
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            codes.add(langEntry.getValue().getCode());
            addDropDownElement(langEntry.getValue().getCode(), langEntry.getValue().getName());
        }
    }

    @Override
    protected void doSetDefaultValue(final String value) {
        if (codes.contains(value)) {
            super.doSetDefaultValue(value);
        } else {
            super.doSetDefaultValue(DEFAULT_LANG);
        }
    }

    /**
     * Sets the default value with the first non null string value from
     * <code>langs</code>
     */
    public void setDefaultValue(final String... langs) {
        if (langs == null) {
            throw new NullPointerException("Noob programmer !");
        }
        for (final String lang : langs) {
            if (lang != null && !lang.isEmpty()) {
                setDefaultStringValue(lang);
                return;
            }
        }
        setDefaultStringValue("en");
    }
}
