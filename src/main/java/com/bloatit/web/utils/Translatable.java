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

package com.bloatit.web.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.bloatit.model.Translation;

/**
 * Abstract class that describes components that can have various translations
 */
public class Translatable {
    private Map<Locale, Translation> translations;


    /**
     * Create a translatable with a default translation and a default language
     * @param entry
     * @param defaultLocale
     */
    public Translatable(Translation entry){
        this.translations = new HashMap<Locale, Translation>();
        this.translations.put(entry.getLocale(), entry);
    }

    /**
     * Creates a Translatable from a list of available translations
     * @param translationList
     */
    public Translatable(List<Translation> translationList){
        this.translations = new HashMap<Locale, Translation>();
        for(Translation t : translationList ){
            this.translations.put(t.getLocale(), t);
        }
    }

    /**
     * Adds a translation to a
     * @param translation
     */
    public void addTranslation(Translation translation){
        this.translations.put(translation.getLocale(), translation);
    }

    /**
     * Find a translation for a given language
     * @param lang the language for which the translation is desired
     * @return the translation matching lang.
     */
    public Translation getTranslationForLang(Locale lang){
        if(!this.containsLang(lang)){
            return null;
        }
        return this.translations.get(lang);
    }

    /**
     * indicated wether the item have been translated to a given lang
     * @param lang the language
     * @return <i>true</i> if it has been translated to lang <i>false</i> otherwise
     */
    public boolean containsLang(Locale lang){
        return translations.containsKey(lang);
    }

    /**
     * @return the list of available languages for the translation
     */
    public Set<Locale> getAvailableLangs(){
        return this.translations.keySet();
    }


}