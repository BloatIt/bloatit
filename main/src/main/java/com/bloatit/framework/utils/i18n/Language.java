/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.utils.i18n;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.bloatit.common.Log;
import com.bloatit.framework.LocalesConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.highlevel.MeanUserException;
import com.bloatit.framework.webprocessor.components.form.DropDownElement;

/**
 * <p>
 * Represents language.
 * </p>
 * <p>
 * Examples :
 * <li>France -> fr</li>
 * <li>England -> en</li>
 * <li>Deutschland -> de</li>
 * </p>
 */
public enum Language {
    EN("en"),
    FR("fr");
    
    private final String code;
    
    
    /**
     * Creates a new language
     * 
     * @param code the ISO code of the language
     */
    Language(final String code) {
        this.code = code;
    }
    
    /**
     * Creates a new language
     * 
     * @param locale
     */
    public static Language fromLocale(final Locale locale) {
        return fromString(locale.getLanguage());
    }
    
    public static Language fromString(String language) {
        for(Language lang : Language.values()) {
            if(lang.getCode().equals(language)) {
                return lang;
            }
        }
        throw new MeanUserException("Unknow language: "+ language);
    }

    /**
     * @return the ISO code of the language
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the Locale matching the language
     */
    public Locale getLocale() {
        return new Locale(code);
    }

    

}
