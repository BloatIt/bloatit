package com.bloatit.framework.utils.i18n;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.helpers.MessageFormatter;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import com.bloatit.common.Log;
import com.bloatit.common.PropertyLoader;
import com.bloatit.framework.exceptions.general.BadProgrammerException;
import com.bloatit.framework.exceptions.general.ExternalErrorException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.form.DropDownElement;
import com.bloatit.model.Member;

// TODO break dependency to Member.

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
public final class Localizator {
    /** Path of the files containing available languages */
    private static final String LANGUAGES_PATH = "i18n/languages";
    /** For parsing of available languages file */
    private static final String LANGUAGE_CODE = "code";
    /** Default user locale */
    private static final Locale DEFAULT_LOCALE = new Locale("en", "US");

    private static final String SEPARATORS_REGEX = "[_-]";

    private static Map<String, LanguageDescriptor> availableLanguages = Collections.unmodifiableMap(initLanguageList());

    private Locale locale;
    private final I18n i18n;
    private final String urlLang;
    private final List<String> browserLangs;

    // translations cache
    private static final Map<Locale, I18n> localesCache = Collections.synchronizedMap(new HashMap<Locale, I18n>());

    public Localizator(final String urlLang, final List<String> browserLangs) {
        this.urlLang = urlLang;
        this.browserLangs = browserLangs;
        this.locale = inferLocale();

        if (localesCache.containsKey(locale)) {
            this.i18n = localesCache.get(locale);
        } else {
            this.i18n = I18nFactory.getI18n(Localizator.class, "i18n.Messages", locale);
            localesCache.put(locale, i18n);
        }
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
     * Shortcut for getLangyageCode()
     * 
     * @see #getLanguageCode()
     */
    public String getCode() {
        return locale.getLanguage();
    }

    /**
     * @return the ISO code for the language
     */
    public String getLanguageCode() {
        return locale.getLanguage();
    }

    /**
     * @return the ISO code for the language
     */
    public String getCountryCode() {
        return locale.getCountry();
    }

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
    public String tr(final String toTranslate) {
        return i18n.tr(toTranslate);
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
     * For more examples see : {@link http
     * ://code.google.com/p/gettext-commons/wiki/Tutorial} </p>
     * 
     * @param toTranslate the String to translate
     * @param parameters the list of parameters that will be inserted into the
     *            string
     * @return the translated String
     * @see #tr(String)
     * @see #trn(String, String, long, Object...)
     * @see MessageFormatter
     */
    public String tr(final String toTranslate, final Object... parameters) {
        return i18n.tr(toTranslate, parameters);
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
        return i18n.trn(singular, plural, amount);
    }

    /**
     * <p>
     * Translates a parametered-constant string, and handles plural
     * </p>
     * <p>
     * Uses {@link MessageFormatter#format(String, Object, Object)} to format
     * </p>
     * <p>
     * Example <br>
     * <code>System.out.println(i18n.trn("Night {0} of 1001",
     * "More than 1001 nights! {0} already!", 1002, new Integer(1024)));<br> // Will print
     * "More than 1001 nights! 1024 already!"</code>
     * </p>
     * <p>
     * For more examples see : {@link http
     * ://code.google.com/p/gettext-commons/wiki/Tutorial}
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
     * @see MessageFormatter
     */
    public String trn(final String singular, final String plural, final long amount, final Object... parameters) {
        return i18n.trn(singular, plural, amount, parameters);
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
     * For more examples see : {@link http
     * ://code.google.com/p/gettext-commons/wiki/Tutorial}
     * </p>
     * 
     * @param context the context of the text to be translated
     * @param text the ambiguous key message in the source locale
     * @return <code>text</code> if the locale of the underlying resource bundle
     *         equals the source code locale, the disambiguated translation of
     *         <code>text</code> otherwise
     * @see #setSourceCodeLocale(Locale)
     */
    public String trc(final String context, final String text) {
        return i18n.trc(context, text);
    }

    /**
     * <p>
     * Finds all available languages for the system
     * <p>
     * <p>
     * Returns a map with [<language english name>:[<language local
     * name><language ISO code>]] Example : [French:[Français,fr]] or
     * [English:[English,en]]
     * </p>
     * 
     * @return a list with all the language descriptors
     */
    public static Map<String, LanguageDescriptor> getAvailableLanguages() {
        return availableLanguages;
    }

    /**
     * Parses the languages file and initializes the list of available languages
     * Used in the init of the static field.
     */
    private static Map<String, LanguageDescriptor> initLanguageList() {
        final Map<String, LanguageDescriptor> languages = new HashMap<String, Localizator.LanguageDescriptor>();
        final Properties properties;
        
        try {
            properties = PropertyLoader.loadProperties(LANGUAGES_PATH);
        } catch (final IOException e) {
            throw new BadProgrammerException("File describing available languages is not available at " + LANGUAGES_PATH, e);
        }
        for (final Entry<?, ?> property : properties.entrySet()) {
            final String key = (String) property.getKey();
            final String value = (String) property.getValue();

            // Remove the .code or .name
            final String lang = key.substring(0, key.lastIndexOf('.'));

            LanguageDescriptor ld;
            if (!languages.containsKey(lang)) {
                ld = new LanguageDescriptor();
                languages.put(lang, ld);
            } else {
                ld = languages.get(lang);
            }

            if (key.endsWith("." + LANGUAGE_CODE)) {
                ld.code = value;
            } else {
                ld.name = value;
            }
        }

        return languages;
    }

    /**
     * DO nothing !!
     */
    public void setUserFavorite() {
        assert false;
    }

    /**
     * Describes a Language using a two letters code and a name
     */
    public static class LanguageDescriptor implements DropDownElement {
        public String code;
        public String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getCode() {
            return code;
        }
    }

    /**
     * Gets the date pattern that matches the current user language in
     * <i>SHORT</i> format, i.e. : dd/mm/yyyy if locale is french, or mm/dd/yyyy
     * if locale is english.
     * 
     * @return a String representing the date pattern
     */
    public String getShortDatePattern() {
        return DateLocale.getPattern(locale);
    }

    /**
     * Gets the date pattern that matches the current user language in any
     * format
     * 
     * @param format the format
     * @return the date pattern
     */
    public String getDatePattern(final FormatStyle format) {
        return DateLocale.getPattern(locale, format);
    }

    /**
     * Returns a DateLocale representing the string version of the date
     */
    public DateLocale getDate(final String dateString) throws DateParsingException {
        return new DateLocale(dateString, locale);
    }

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

    /**
     * <p>
     * Forces the current locale to the member user choice.
     * </p>
     * <p>
     * Use whenever the user explicitely asks to change the locale setting back
     * to his favorite, or when he logs in
     * </p>
     */
    public void forceMemberChoice() {
        final Member member = Context.getSession().getAuthToken().getMember();
        locale = member.getLocaleUnprotected();
    }

    /**
     * Infers the locale based on various parameters
     */
    private Locale inferLocale() {
        Locale locale = null;

        if (urlLang != null && !urlLang.equals("default")) {

            // Default language
            String country;
            if (Context.getSession().getAuthToken() != null) {
                final Member member = Context.getSession().getAuthToken().getMember();
                country = member.getLocaleUnprotected().getCountry();
            } else {
                country = browserLocaleHeuristic().getCountry();
            }
            locale = new Locale(urlLang, country);

            boolean found = false;
            for (final Locale availablelocale : Locale.getAvailableLocales()) {
                if (urlLang.equals(availablelocale.getLanguage())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Log.framework().error("Strange language code " + urlLang);
            }

        } else {
            // Other cases
            if (Context.getSession().getAuthToken() != null) {
                final Member member = Context.getSession().getAuthToken().getMember();
                locale = member.getLocaleUnprotected();
            } else {
                locale = browserLocaleHeuristic();
            }
        }
        return locale;
    }

    /**
     * <p>
     * Finds the dominant Locale for the user based on the browser transmitted
     * parameters
     * </p>
     * <p>
     * This method use preferences based on data transmitted by browser, but
     * will always try to fetch a locale with a language and a country.
     * </p>
     * <p>
     * Cases are :
     * <li>The favorite locale has language and country : it is the selected
     * locale</li>
     * <li>The favorite locale has a language but no country : will try to
     * select another locale with the <b>same language</b></li>
     * <li>If no locale has a country, the favorite language as of browser
     * preference will be used, and country will be set as US. If no language is
     * set, the locale will be set using DEFAULT_LOCALE (currently en_US).
     * </p>
     * 
     * @return the favorite user locale
     */
    private Locale browserLocaleHeuristic() {
        Locale currentLocale = null;
        float currentWeigth = 0;
        Locale favLanguage = null;
        float favLanguageWeigth = 0;
        Locale favCountry = null;
        float favCountryWeigth = 0;

        for (final String lang : browserLangs) {
            final String[] favLangs = lang.split(";");

            float weigth;
            if (favLangs.length > 1) {
                weigth = new Float(favLangs[1].substring("q=".length()));
            } else {
                weigth = 1;
            }

            final String favLang[] = favLangs[0].split(SEPARATORS_REGEX);

            Locale l;
            if (favLang.length < 2) {
                l = new Locale(favLang[0]);
            } else {
                l = new Locale(favLang[0], favLang[1]);
            }

            if (!l.getLanguage().isEmpty() && l.getCountry().isEmpty()) {
                // New FavoriteLanguage
                if (favLanguageWeigth < weigth) {
                    favLanguageWeigth = weigth;
                    favLanguage = l;
                }
            }
            if (!l.getLanguage().isEmpty() && !l.getCountry().isEmpty()) {
                // New currentLocale
                if (currentWeigth < weigth) {
                    currentWeigth = weigth;
                    currentLocale = l;
                }
            }
            if (l.getLanguage().isEmpty() && !l.getCountry().isEmpty()) {
                // New currentCountry
                if (favCountryWeigth < weigth) {
                    favCountryWeigth = weigth;
                    favCountry = l;
                }
            }
        }

        if (currentLocale == null && favLanguage == null) {
            return DEFAULT_LOCALE;
        }
        if (currentLocale != null && favLanguage == null) {
            return currentLocale;
        }
        if (currentLocale == null && favLanguage != null) {
            if (favCountry == null) {
                favCountry = Locale.US;
            }
            return new Locale(favLanguage.getLanguage(), favCountry.getCountry());
        }

        // Case where both CurrentLocale != null && FavLanguage != null
        return currentLocale;
    }
}
