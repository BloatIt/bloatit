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

    private static Map<String, Locale> languageCode = new HashMap<String, Locale>() {

        /**
         * 
         */
        private static final long serialVersionUID = 9019121360450239715L;

        {
            put("en", Locale.ENGLISH);
            put("en-us", Locale.ENGLISH);
            put("fr", Locale.FRENCH);
            put("fr-fr", Locale.FRENCH);
        }
    };
    private static Map<Locale, LanguageTemplate> languageList = new HashMap<Locale, LanguageTemplate>() {

        /**
         * 
         */
        private static final long serialVersionUID = -2144420475764085797L;

        {
            put(Locale.ENGLISH, new LanguageTemplate("en", "English", Locale.ENGLISH));
            put(Locale.FRENCH, new LanguageTemplate("fr", "Français", Locale.FRENCH));
            put(Locale.GERMAN, new LanguageTemplate("de", "German", Locale.GERMAN));

        }
    };
    private LanguageTemplate template;

    public Language() {
        this.template = languageList.get(Locale.ENGLISH);
    }

    public Language(Locale code) {
        this.template = languageList.get(code);
        if (this.template == null) {
            this.template = languageList.get(Locale.ENGLISH);
        }
    }

    public String getCode() {
        return template.locale.getLanguage();
    }

    public void findPrefered(List<String> preferredLangs) {
        Locale locale;
        for (final String preferredLang : preferredLangs) {
            final String lang = preferredLang.split(";")[0];
            if (Language.languageCode.containsKey(lang)) {
                locale = Language.languageCode.get(lang);
                if (languageList.containsKey(locale)) {
                    template = languageList.get(locale);
                    break;
                } else {
                    System.err.println("Unknow language code" + locale);
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

        final public Locale locale;
        final public String label;
        final public I18n i18n;

        private LanguageTemplate(String key, String label, Locale locale) {
            this.label = label;
            this.locale = locale;
            i18n = I18nFactory.getI18n(Language.class, "i18n.Messages", locale);

        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + (this.locale != null ? this.locale.hashCode() : 0);
            hash = 59 * hash + (this.label != null ? this.label.hashCode() : 0);
            hash = 59 * hash + (this.i18n != null ? this.i18n.hashCode() : 0);
            return hash;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Language other = (Language) obj;
        if (this.template != other.template && (this.template == null || !this.template.equals(other.template))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.template != null ? this.template.hashCode() : 0);
        return hash;
    }

}
