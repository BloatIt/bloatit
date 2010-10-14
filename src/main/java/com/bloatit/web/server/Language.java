/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.server;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class Language {

    private static Map<String, String> languageCode = new HashMap<String, String>() {

        {
            put("en", "en");
            put("en-us", "en");
            put("fr", "fr");
            put("fr-fr", "fr");
        }
    };
    private static Map<String, LanguageTemplate> languageList = new HashMap<String, LanguageTemplate>() {

        {
            put("en", new LanguageTemplate("en", "English", java.util.Locale.ENGLISH));
            put("fr", new LanguageTemplate("fr", "Français", java.util.Locale.FRENCH));

        }
    };
    private LanguageTemplate template;

    public Language() {
        template = languageList.get("en");
    }

    public String getCode() {
        return template.key;
    }

    public void findPrefered(List<String> preferredLangs) {
        String code;
        for (String preferredLang : preferredLangs) {
            String lang = preferredLang.split(";")[0];
            if (Language.languageCode.containsKey(lang)) {
                code = Language.languageCode.get(lang);
                if (languageList.containsKey(code)) {
                    template = languageList.get(code);
                    break;
                } else {
                    System.err.println("Unknow language code" + code);
                }
            } else {
                System.err.println("Unknow language" + lang);
                // TODO: Clean log
            }
        }
    }

    public String tr(String s) {
        return template.i18n.tr(s);
    }

    void setCode(String code) {
        if (languageList.containsKey(code)) {
            template = languageList.get(code);
        } else {
            System.err.println("Unknow language code" + code);
        }
    }

    /**
     * Nested class to handle different languages
     */
    private static class LanguageTemplate {

        final public String key;
        final public String label;
        final public I18n i18n;

        private LanguageTemplate(String key, String label, Locale locale) {
            this.label = label;
            this.key = key;
            i18n = I18nFactory.getI18n(Language.class, "i18n.Messages", locale);

        }
    }
}
