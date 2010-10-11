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

import java.util.ArrayList;
import java.util.HashMap;

public class Language {

    private static HashMap<String, String> languageCode = new HashMap<String, String>() {
        {
            put("en", "en");
            put("en-us", "en");
            put("fr", "fr");
            put("fr-fr", "fr");
        }
    };

    private static HashMap<String, LanguageCode> LanguageList = new HashMap<String, LanguageCode>() {
        {
            put("en", new LanguageCode("en_US.utf8", true, "English", "en"));
            put("fr", new LanguageCode("fr_FR.utf8", true, "Français", "fr"));
            put("fr-fr", new LanguageCode("fr_FR.utf8", false, "fr"));
            put("fr-be", new LanguageCode("fr_FR.utf8", false, "fr"));
            put("fr-ca", new LanguageCode("fr_FR.utf8", false, "fr"));
            put("fr-lu", new LanguageCode("fr_FR.utf8", false, "fr"));
            put("fr-ch", new LanguageCode("fr_FR.utf8", false, "fr"));
        }
    };

    private String code;

    public Language(){
        this.code = "en";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void findPrefered(ArrayList<String> preferredLangs){
        for ( String preferredLang : preferredLangs){
            String lang = preferredLang.split(";")[0];
            if(Language.languageCode.containsKey(lang)){
                this.code = Language.languageCode.get(lang);
            }else{
                System.err.println("Unknow language code "+lang);
                // TODO: Clean log
            }
        }
    }

    public String getText(String s){
        // TODO: everything
        //lang = gettext.translation(config.get("gettext_package"), config.get("localedir"), languages=[self.code], codeset="UTF-8")
        //return lang.gettext
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Nested class to handle different languages
     * [Equivalent to using a struct]
     */
    private static class LanguageCode {

        public String key;
        public boolean choosable;
        public String label;
        public String ref;

        public LanguageCode(String key, boolean choosable, String ref) {
            this.key = key;
            this.choosable = choosable;
            this.ref = ref;
        }

        public LanguageCode(String key, boolean choosable, String label, String ref) {
            this(key, choosable, ref);
            this.label = label;
        }
    }
}