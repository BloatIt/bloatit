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
package com.bloatit.web.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;
import com.bloatit.web.utils.PropertyLoader;

public class Language {
	
	private static final String LANGUAGES_PATH = "i18n/languages";
	private static final String LANGUAGE_CODE = "code";
	private static final String LANGUAGE_NAME = "name";

	private static Map<String, Locale> languageCode = new HashMap<String, Locale>() {
		private static final long serialVersionUID = 9019121360450239715L;
		{
			put("en", Locale.ENGLISH);
			put("en-us", Locale.ENGLISH);
			put("fr", Locale.FRENCH);
			put("fr-fr", Locale.FRENCH);
		}
	};

	private static Map<Locale, LanguageTemplate> languageList = new HashMap<Locale, LanguageTemplate>() {
		private static final long serialVersionUID = -2144420475764085797L;
		{
			put(Locale.ENGLISH, new LanguageTemplate("en", "English",
					Locale.ENGLISH));
			put(Locale.FRENCH, new LanguageTemplate("fr", "Français",
					Locale.FRENCH));
			put(Locale.GERMAN, new LanguageTemplate("de", "German",
					Locale.GERMAN));

		}
	};

	private static Map<String, LanguageDescriptor> availableLanguages;

	private LanguageTemplate template;

	public Language() {
		template = languageList.get(Locale.ENGLISH);
	}

	public Language(final Locale code) {
		template = languageList.get(code);
		if (template == null) {
			template = languageList.get(Locale.ENGLISH);
		}
	}

	public String getCode() {
		return template.locale.getLanguage();
	}

	public void findPrefered(final List<String> preferredLangs) {
		Locale locale;
		for (final String preferredLang : preferredLangs) {
			final String lang = preferredLang.split(";")[0];
			if (Language.languageCode.containsKey(lang)) {
				locale = Language.languageCode.get(lang);
				if (languageList.containsKey(locale)) {
					template = languageList.get(locale);
					break;
				} else {
					System.err.println("Unknow language code: " + locale);
				}
			} else {
				Log.web().warn("Unknow language: " + lang);
			}
		}
	}

	public Locale getLocale() {
		return template.locale;
	}

	public String tr(final String s) {
		return template.i18n.tr(s);
	}

	public String tr(final String s, final Object[] objects) {
		return template.i18n.tr(s, objects);
	}

	void setCode(final String code) {
		if (languageCode.containsKey(code)) {
			template = languageList.get(languageCode.get(code));
		} else {
			System.err.println("Unknow language code: " + code);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Language other = (Language) obj;
		if (template != other.template
				&& (template == null || !template.equals(other.template))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 11 * hash + (template != null ? template.hashCode() : 0);
		return hash;
	}

	/**
	 * Finds all available languages
	 * 
	 * @return a list with all the language descriptors
	 */
	public static Map<String, Language.LanguageDescriptor> getAvailableLanguages() {
		if (availableLanguages == null) {
			availableLanguages = new HashMap<String, Language.LanguageDescriptor>();
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
			for(Entry<?, ?> property : properties.entrySet()){
				String key = (String)property.getKey();
				String value = (String)property.getValue();
				
				// Remove the .code or .name
				String lang = key.substring(0, key.lastIndexOf("."));
				
				LanguageDescriptor ld;
				if(!availableLanguages.containsKey(lang)){
					ld = new LanguageDescriptor();
					availableLanguages.put(lang, ld);
				}else{
					ld = availableLanguages.get(lang);
				}
				
				if(key.endsWith("."+ LANGUAGE_CODE)){
					ld.code = value;
				}else{
					ld.name = value;
				}
			}
		} catch (IOException e) {
			throw new FatalErrorException("File describing available languages is not available at "+LANGUAGES_PATH, e);
		}
	}

	/**
	 * Describes a Language using a two letters code and a name
	 */
	public static class LanguageDescriptor {
		public String code;
		public String name;
	}

	/**
	 * Nested class to handle different languages
	 */
	private static class LanguageTemplate {

		final public Locale locale;
		final public String label;
		final public I18n i18n;

		private LanguageTemplate(final String key, final String label,
				final Locale locale) {
			this.label = label;
			this.locale = locale;
			i18n = I18nFactory.getI18n(Language.class, "i18n.Messages", locale);
		}

		@Override
		public boolean equals(final Object obj) {
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
			hash = 59 * hash + (locale != null ? locale.hashCode() : 0);
			hash = 59 * hash + (label != null ? label.hashCode() : 0);
			hash = 59 * hash + (i18n != null ? i18n.hashCode() : 0);
			return hash;
		}
	}
}
