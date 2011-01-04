/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.utils;

import java.util.List;

import com.bloatit.framework.Translation;
import com.bloatit.web.utils.i18n.Language;

/**
 * Helps manipulating translations
 */
public class TranslationManipulator {
    private final List<Language> preferredLocales;

    public TranslationManipulator(final List<Language> preferredLocales) {
        this.preferredLocales = preferredLocales;
    }

    /**
     * Get the element in the favorite language of the user
     * 
     * @return the element in the good language or null if there is no match
     */
    public Translation getPreferedTranslation(final Translatable toTranslate) {
        for (final Language lang : preferredLocales) {
            if (toTranslate.containsLang(lang)) {
                return toTranslate.getTranslationForLang(lang);
            }
        }
        return null;
    }

    /**
     * Shorter version of the getPreferedTranslation
     * 
     * @see TranslationManipulator.getPreferedTranslation()
     */
    public Translation tr(final Translatable toTranslate) {
        return getPreferedTranslation(toTranslate);
    }
}
