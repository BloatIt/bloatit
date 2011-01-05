package com.bloatit.web.utils.i18n;

import java.io.IOException;
import java.math.BigDecimal;
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

import com.bloatit.common.FatalErrorException;
import com.bloatit.framework.Member;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.PropertyLoader;

/**
 * <p>
 * Class that encapsulates all translation tools
 * </p>
 * <p>
 * Tools provided are :
 * <li>All static translation tools (for the UI) implemented with gettext</li>
 * <li>All Dynamic translation tools (for the content), mostly for dates and currencies
 * TODO and time</li>
 * <p>
 * Class is immutable, if you need to change locale, create a new object
 * </p>
 * </p>
 */
public class Localizator {
	/** Path of the files containing available languages */
	private static final String LANGUAGES_PATH = "i18n/languages";
	/** For parsing of available languages file */
	private static final String LANGUAGE_CODE = "code";
	/** Default user locale */
	private static final Locale DEFAULT_LOCALE = new Locale("en_US");

	private static Map<String, LanguageDescriptor> availableLanguages;

	private Locale locale;
	private final I18n i18n;
	private String urlLang;
	private List<String> browserLangs;

	public Localizator(String urlLang, List<String> browserLangs) {
		this.urlLang = urlLang;
		this.browserLangs = browserLangs;
		this.locale = inferLocale();
		this.i18n = I18nFactory.getI18n(Language.class, "i18n.Messages", locale);
	}


	/**
	 * Returns the Locale for the localizator
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Shortcut for getLangyageCode()
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
	 * Returns <code>toTranslate</code> translated into the currently selected language.
	 * Every user-visible string in the program must be wrapped into this function
	 * </p>
	 * @param toTranslate the string to translate
	 * @return the translated string
	 */
	public String tr(String toTranslate) {
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
	 * Returns <code>toTranslate</code> and replaces the string with the parameters
	 * </p>
	 * <p>
	 * One example :
	 * <p>
	 * <code>i18n.tr("foo {0} bar", new Integer(1024)));<br> //Will print
	 * "foo 1024 bar"</code>
	 * </p>
	 * For more examples see : {@link http
	 * ://code.google.com/p/gettext-commons/wiki/Tutorial} </p>
	 * @param toTranslate the String to translate
	 * @param parameters the list of parameters that will be inserted into the string
	 * @return the translated String
	 * @see #tr(String)
	 * @see #trn(String, String, long, Object...)
	 * @see MessageFormatter
	 */
	public String tr(String toTranslate, Object... parameters) {
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
	 * @param singular The singular version of the displayed string
	 * @param plural the plural version of the displayed string
	 * @param amount the <i>amount</i> of elements, 0 or 1 will be singular, >1 will be
	 *        plural
	 * @return the translated <i>singular</i> or <i>plural</i> string depending on value
	 *         of <code>amount</code>
	 * @see #tr(String)
	 */
	public String trn(String singular, String plural, long amount) {
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
	 * @param singular The singular string
	 * @param plural the plural string
	 * @param amount the <i>amount</i> of elements, 0 or 1 will be singular, >1 will be
	 *        plural
	 * @param parameters the list of parameters that will be replaced into the String
	 * @return the translated <i>singular</i> or <i>plural</i> string depending on value
	 *         of <code>amount</code>, with the <code>parameters</code> inserted.
	 * @see #trn(String, String, long)
	 * @see MessageFormatter
	 */
	public String trn(String singular, String plural, long amount, Object... parameters) {
		return i18n.trn(singular, plural, amount, parameters);
	}

	/**
	 * <p>
	 * Finds all available languages for the system
	 * <p>
	 * <p>
	 * Returns a map with [<language english name>:[<language local name><language ISO
	 * code>]] Example : [French:[Français,fr]] or [English:[English,en]]
	 * </p>
	 * @return a list with all the language descriptors
	 */
	public static Map<String, LanguageDescriptor> getAvailableLanguages() {
		if (availableLanguages == null) {
			availableLanguages = new HashMap<String, LanguageDescriptor>();
			initLanguageList();
		}
		return availableLanguages;
	}

	/**
	 * Parses the languages file and initializes the list of available languages
	 */
	private static void initLanguageList() {
		try {
			Properties properties = PropertyLoader.loadProperties(LANGUAGES_PATH);
			for (Entry<?, ?> property : properties.entrySet()) {
				String key = (String) property.getKey();
				String value = (String) property.getValue();

				// Remove the .code or .name
				String lang = key.substring(0, key.lastIndexOf("."));

				LanguageDescriptor ld;
				if (!availableLanguages.containsKey(lang)) {
					ld = new LanguageDescriptor();
					availableLanguages.put(lang, ld);
				} else {
					ld = availableLanguages.get(lang);
				}

				if (key.endsWith("." + LANGUAGE_CODE)) {
					ld.code = value;
				} else {
					ld.name = value;
				}
			}
		} catch (IOException e) {
			throw new FatalErrorException("File describing available languages is not available at " + LANGUAGES_PATH, e);
		}
	}

	public void setUserFavorite(){
		
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
				Member member = Context.getSession().getAuthToken().getMember();
				member.authenticate(Context.getSession().getAuthToken());
				country = member.getLocale().getCountry();
			} else {
				country = browserLocaleHeuristic().getCountry();
			}
			locale = new Locale(urlLang, country);
		} else {
			// Other cases
			if (Context.getSession().getAuthToken() != null) {
				Member member = Context.getSession().getAuthToken().getMember();
				member.authenticate(Context.getSession().getAuthToken());
				locale = member.getLocale();
			} else {
				locale = browserLocaleHeuristic();
			}
		}
		return locale;
	}
	
	/**
	 * <p>
	 * Finds the dominant Locale for the user based on the browser transmitted parameters
	 * </p>
	 * <p>
	 * This method use preferences based on data transmitted by browser, but will always
	 * try to fetch a locale with a language and a country.
	 * </p>
	 * <p>
	 * Cases are :
	 * <li>The favorite locale has language and country : it is the selected locale</li>
	 * <li>The favorite locale has a language but no country : will try to select another
	 * locale with the <b>same language</b></li>
	 * <li>If no locale has a country, the favorite language as of browser preference will
	 * be used, and country will be set as US. If no language is set, the locale will be
	 * set using DEFAULT_LOCALE (currently en_US).
	 * </p>
	 * @return the favorite user locale
	 */
	private Locale browserLocaleHeuristic() {
		Locale currentLocale = null;
		float currentWeigth = 0;
		Locale favLanguage = null;
		float favLanguageWeigth = 0;
		Locale favCountry = null;
		float favCountryWeigth = 0;

		for (String lang : browserLangs) {
			String[] favLangs = lang.split(";");

			float weigth;
			if (favLangs.length > 1) {
				weigth = new Float(favLangs[1].substring("q=".length()));
			} else {
				weigth = 1;
			}

			Locale l = new Locale(favLangs[0]);

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
	
	/**
	 * Describes a Language using a two letters code and a name
	 */
	public static class LanguageDescriptor {
		public String code;
		public String name;
	}
	
	/**
	 * Gets the date pattern that matches the current user language in <i>SHORT</i>
	 * format, i.e. : dd/mm/yyyy if locale is french, or mm/dd/yyyy if locale is english.
	 * @return a String representing the date pattern
	 */
	public String getDatePattern() {
		return DateLocale.getPattern(Context.getLocalizator().getLocale());
	}
	
	/**
	 * Returns a DateLocale representing the string version of the date
	 */
	public DateLocale getDate(String dateString) throws DateParsingException{
		return new DateLocale(dateString, locale);
	}
	
	/**
	 * Returns a DateLocale encapsulating the java date
	 * Use to display any date
	 */
	public DateLocale getDate(Date date){
		return new DateLocale(date, locale);
	}
	
	/**
	 * Returns a CurrencyLocale to work on <code>euroAmount</code>
	 */
	public CurrencyLocale getCurrency(BigDecimal euroAmount){
		return new CurrencyLocale(euroAmount, locale);
	}

	/**
	 * <p>Forces the current locale to the member user choice.</p>
	 * <p>Use whenever the user explicitely asks to change the locale setting back to his 
	 * favorite, or when he logs in</p>
	 */
	public void forceMemberChoice() {
	    Member member = Context.getSession().getAuthToken().getMember();
	    member.authenticate(Context.getSession().getAuthToken());
		locale = member.getLocale();
    }
}
