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

import com.bloatit.model.Translatable;
import com.bloatit.model.Translation;
import com.bloatit.web.server.Language;
import java.util.List;

/**
 * Helps manipulating translations
 */
public class TranslationManipulator {
    private final List<Language> preferredLanguages;

    public TranslationManipulator( List<Language> preferredLanguages){
        this.preferredLanguages = preferredLanguages;
    }
    
    /**
     * Get the element in the favorite language of the user
     * @return the element in the good language or null if there is no match
     */
    public Translation getPreferedTranslation(Translatable toTranslate){
        for(Language lang : this.preferredLanguages){
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