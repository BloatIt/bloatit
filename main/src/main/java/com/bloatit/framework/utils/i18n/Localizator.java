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
package com.bloatit.framework.utils.i18n;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.Translator;
import com.bloatit.framework.xcgiserver.HttpHeader;

/**
 * <p>
 * Class that encapsulates all translation tools
 * </p>
 * <p>
 * Tools provided are :
 * <li>All static translation tools (for the UI) implemented with gettext</li>
 * <li>All Dynamic translation tools (for the content), mostly for dates,
 * currencies and time</li>
 * <p>
 * Class is immutable, if you need to change locale, create a new object
 * </p>
 * </p>
 */
public final class Localizator implements Translator {
    /** Default user locale */
    private static final Locale DEFAULT_LOCALE = new Locale("en", "US");
    /** Translation cache */
    private static final Map<String, I18n> localesCache = Collections.synchronizedMap(new HashMap<String, I18n>());

    static {
        // By default, the Java default language is used as a fallback for
        // gettext.
        // We override this behavior by setting the default locale to english
        Locale.setDefault(new Locale("en", "US"));
    }

    private Locale locale;
    private I18n i18n;

    public Localizator(final Locale loc) {
        this.locale = loc;
        this.i18n = getI18n(locale);
    }

    public Localizator(HttpHeader header) {
        if (header.getPageLanguage().equals(HttpHeader.DEFAULT_LANG)) {
            this.locale = header.getHttpAcceptLanguage().getPreferedLocal();
        } else {
            this.locale = new Locale(header.getPageLanguage(), header.getHttpAcceptLanguage().getPreferedLocal().getCountry());
        }
        this.i18n = getI18n(locale);
    }

    /**
     * Force the language part of a locale to a specific one
     */
    public void forceLanguage(final Locale language) {
        locale = new Locale(language.getLanguage(), locale.getCountry());
        this.i18n = getI18n(locale);
    }

    /**
     * Force the locale to a specific locale
     */
    public void forceLocale(final Locale loc) {
        locale = loc;
        this.i18n = getI18n(locale);
    }

    /**
     * Returns the Locale for the localizator
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the ISO code for the language
     */
    public String getCountryCode() {
        return locale.getCountry();
    }

    // ////////////////////
    // Working on the language part of the locale

    /**
     * @return the ISO code for the language
     */
    public String getLanguageCode() {
        return locale.getLanguage();
    }

    // ////////////////////////
    // Some utility functions

    /**
     * Returns a DateLocale encapsulating the java date Use to display any date
     */
    public DateLocale getDate(final Date date) {
        return new DateLocale(date, locale);
    }

    /**
     * Returns a CurrencyLocale to work on <code>euroAmount</code>
     */
    public CurrencyLocale getCurrency(final BigDecimal euroAmount) {
        try {
            return new CurrencyLocale(euroAmount, locale);
        } catch (final CurrencyNotAvailableException e) {
            try {
                return new CurrencyLocale(euroAmount, DEFAULT_LOCALE);
            } catch (final CurrencyNotAvailableException e1) {
                throw new BadProgrammerException("Fallback locale for currency " + DEFAULT_LOCALE.getLanguage() + "_" + DEFAULT_LOCALE.getCountry()
                        + "not available", e);
            }
        }
    }

    public NumberFormat getNumberFormat() {
        return NumberFormat.getInstance(getLocale());
    }

    // /////////////////////////////////////////////
    // TR system (gnu gettext)
    // ////////////////////////////////////////////

    /**
     * <p>
     * Translates a constant String
     * </p>
     * <p>
     * Returns <code>toTranslate</code> translated into the currently selected
     * language. Every user-visible string in the program must be wrapped into
     * this function
     * </p>
     *
     * @param toTranslate the string to translate
     * @return the translated string
     */
    @Override
    public String tr(final String toTranslate) {
        return correctTr(i18n.tr(toTranslate));
    }

    /**
     * <p>
     * Translates a parametered constant string
     * </p>
     * <p>
     * In <b>MOST CASES</b> the plural handling version should be used : see
     * {@link #trn(String, String, long, Object...)}
     * </p>
     * <p>
     * Returns <code>toTranslate</code> and replaces the string with the
     * parameters
     * </p>
     * <p>
     * One example :
     * <p>
     * <code>i18n.tr("foo {0} bar", new Integer(1024)));<br> //Will print
     * "foo 1024 bar"</code>
     * </p>
     * For more examples see :
     * {@link "http://code.google.com/p/gettext-commons/wiki/Tutorial"} </p>
     *
     * @param toTranslate the String to translate
     * @param parameters the list of parameters that will be inserted into the
     *            string
     * @return the translated String
     * @see #tr(String)
     * @see #trn(String, String, long, Object...)
     * @see org.slf4j.helpers.MessageFormatter
     */
    public String tr(final String toTranslate, final Object... parameters) {
        return correctTr(i18n.tr(toTranslate, parameters));
    }

    /**
     * <p>
     * Translates a constant string using plural
     * </p>
     * <p>
     * Example :
     * <p>
     * <code>System.out.println(i18n.trn("Copied file.", "Copied files.", 4));<br>
     * <code>//will print "Copied files."</code>
     * </p>
     * <p>
     * <code>System.out.println(i18n.trn("Copied file.", "Copied files.", 4));<br> // will
     * print "Copied files."</code>
     * </p>
     * </p>
     *
     * @param singular The singular version of the displayed string
     * @param plural the plural version of the displayed string
     * @param amount the <i>amount</i> of elements, 0 or 1 will be singular, >1
     *            will be plural
     * @return the translated <i>singular</i> or <i>plural</i> string depending
     *         on value of <code>amount</code>
     * @see #tr(String)
     */
    public String trn(final String singular, final String plural, final long amount) {
        if (locale.getLanguage().equals("fr")) {
            // In french, 0 use the singular
            return correctTr(i18n.trn(singular, plural, (amount > 1 ? amount : 1)));
        }
        return correctTr(i18n.trn(singular, plural, amount));
    }

    /**
     * <p>
     * Translates a parametered-constant string, and handles plural
     * </p>
     * <p>
     * Uses
     * {@link org.slf4j.helpers.MessageFormatter#format(String, Object, Object)}
     * to format
     * </p>
     * <p>
     * Example <br>
     * <code>System.out.println(i18n.trn("Night {0} of 1001",
     * "More than 1001 nights! {0} already!", 1002, new Integer(1024)));<br> // Will print
     * "More than 1001 nights! 1024 already!"</code>
     * </p>
     * <p>
     * For more examples see :
     * {@link "http://code.google.com/p/gettext-commons/wiki/Tutorial"}
     * </p>
     *
     * @param singular The singular string
     * @param plural the plural string
     * @param amount the <i>amount</i> of elements, 0 or 1 will be singular, >1
     *            will be plural
     * @param parameters the list of parameters that will be replaced into the
     *            String
     * @return the translated <i>singular</i> or <i>plural</i> string depending
     *         on value of <code>amount</code>, with the <code>parameters</code>
     *         inserted.
     * @see #trn(String, String, long)
     * @see org.slf4j.helpers.MessageFormatter
     */
    public String trn(final String singular, final String plural, final long amount, final Object... parameters) {
        if (locale.getLanguage().equals("fr")) {
            // In french, 0 use the singular
            return correctTr(i18n.trn(singular, plural, (amount > 1 ? amount : 1), parameters));
        }
        return correctTr(i18n.trn(singular, plural, amount, parameters));
    }

    /**
     * Disambiguates translation keys.
     * <p>
     * Sometimes it is necessary to provide different translations of the same
     * word as some words may have multiple meanings in the native language the
     * program is written but not in other languages.
     * </p>
     * <p>
     * Example <br>
     * <code>
     * System.out.println(i18n.trc("chat (verb)", "chat"));<br>
     * System.out.println(i18n.trc("chat (noun)", "chat"));</code>
     * </p>
     * <p>
     * For more examples see :
     * {@link "http://code.google.com/p/gettext-commons/wiki/Tutorial"}
     * </p>
     *
     * @param context the context of the text to be translated
     * @param text the ambiguous key message in the source locale
     * @return <code>text</code> if the locale of the underlying resource bundle
     *         equals the source code locale, the disambiguated translation of
     *         <code>text</code> otherwise
     */
    public String trc(final String context, final String text) {
        return correctTr(i18n.trc(context, text));
    }

    /**
     * Corrects the translated string and make it ready for html
     *
     * @param translation the translated string
     * @return the string ready to be inputed in Html
     */
    private String correctTr(final String translation) {
        return translation.replaceAll("&nbsp;", " ");
    }

    private I18n getI18n(final Locale locale) {
        if (localesCache.containsKey(locale)) {
            return localesCache.get(locale);
        }
        final I18n newI18n = I18nFactory.getI18n(Localizator.class, "i18n.Messages", locale);
        localesCache.put(locale.getLanguage(), newI18n);
        return newI18n;
    }
}
