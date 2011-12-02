//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.components;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.xcgiserver.AvailableLocales;

/**
 * A simple component that proposes the users a drop down menu allowing him to
 * select a language
 */
public class LanguageSelector extends HtmlDropDown {
    private static final String DEFAULT_LANG = "en";
    private final Set<String> codes = new HashSet<String>();

    public LanguageSelector(final String name) {
        super(name);
        for (final Entry<String, String> langEntry : AvailableLocales.getAvailableLangs().entrySet()) {
            codes.add(langEntry.getKey());
            addDropDownElement(langEntry.getKey(), langEntry.getValue());
        }
    }

    public LanguageSelector(final String name, final String label) {
        super(name, label);
        for (final Entry<String, String> langEntry : AvailableLocales.getAvailableLangs().entrySet()) {
            codes.add(langEntry.getKey());
            addDropDownElement(langEntry.getKey(), langEntry.getValue());
        }
    }

    @Override
    protected void doSetDefaultStringValue(final String value) {
        if (codes.contains(value)) {
            super.setDefaultValue(value);
        } else {
            super.setDefaultValue(DEFAULT_LANG);
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
                setDefaultValue(lang);
                return;
            }
        }
        setDefaultValue("en");
    }
}
