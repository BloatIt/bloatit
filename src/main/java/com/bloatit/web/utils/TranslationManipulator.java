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

import java.util.List;
import java.util.Locale;

import com.bloatit.framework.Translation;

/**
 * Helps manipulating translations
 */
public class TranslationManipulator {
    private final List<Locale> preferredLocales;

    public TranslationManipulator( List<Locale> preferredLocales){
        this.preferredLocales = preferredLocales;
    }
    
    /**
     * Get the element in the favorite language of the user
     * @return the element in the good language or null if there is no match
     */
    public Translation getPreferedTranslation(Translatable toTranslate){
        for(Locale lang : this.preferredLocales){
            if(toTranslate.containsLang(lang)){
                return toTranslate.getTranslationForLang(lang);
            }
        }
        return null;
    }

    /**
     * Shorter version of the getPreferedTranslation
     * @see TranslationManipulator.getPreferedTranslation()
     */
    public Translation tr (Translatable toTranslate){
        return this.getPreferedTranslation(toTranslate);
    }
}