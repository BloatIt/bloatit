package com.bloatit.framework.xcgiserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.commons.collections.FastTreeMap;

import com.bloatit.common.Log;

public class AcceptedLanguages {
    @SuppressWarnings("unchecked")
    private final SortedMap<Integer, List<String>> priorityLangs = new FastTreeMap();
    private final Locale preferedLocal;

    AcceptedLanguages(String acceptedLanguages) {

        if (acceptedLanguages != null && acceptedLanguages.length() >= 2) {
            acceptedLanguages = acceptedLanguages.replaceAll("\\[|\\]", "").toLowerCase();
            if (acceptedLanguages.length() >= 2) {
                fillUpPriorityLands(acceptedLanguages);
            }
        }
        if (priorityLangs.isEmpty()) {
            addALang("en", 0);
        }

        preferedLocal = parseAcceptedLanguage();
    }

    /**
     * @param possibleLanguages must be a collection of language only local
     *            ("en" or "fr" or "de"). If There is in the collection a
     *            country info this will not work ("en_US" won't work!)
     * @return the preferred language in the collection, according to the
     *         http_accept_language property, OR "en" if not found.
     */
    public Locale getPreferedLanguageOrEn(Collection<Locale> possibleLanguages) {
        return getPreferedLanguage(possibleLanguages, new Locale("en"));
    }

    /**
     * @param possibleLanguages must be a collection of language only local
     *            ("en" or "fr" or "de"). If There is in the collection a
     *            country info this will not work ("en_US" won't work!)
     * @return the preferred language in the collection, according to the
     *         http_accept_language property, OR <i>defaultLocal</i> if not
     *         found.
     */
    public Locale getPreferedLanguage(Collection<Locale> possibleLanguages, Locale defaultLocal) {

        Locale preferedLanguage = getPreferedLanguage(possibleLanguages);
        if (preferedLanguage != null) {
            return preferedLanguage;
        }
        return defaultLocal;
    }

    /**
     * @param possibleLanguages must be a collection of language only local
     *            ("en" or "fr" or "de"). If There is in the collection a
     *            country info this will not work ("en_US" won't work!)
     * @return the preferred language in the collection, according to the
     *         http_accept_language property, OR <i>NULL</i> if not found.
     */
    public Locale getPreferedLanguage(Collection<Locale> possibleLanguages) {
        for (Entry<Integer, List<String>> langs : priorityLangs.entrySet()) {
            for (String lang : langs.getValue()) {
                Locale locale = new Locale(lang);
                if (possibleLanguages.contains(locale)) {
                    return locale;
                }
            }
        }
        return null;
    }

    public Locale getPreferedLocal() {
        return preferedLocal;
    }

    private Locale parseAcceptedLanguage() {
        // read the list of local and find the best one
        String localLang = null;
        String localCountry = null;
        for (Entry<Integer, List<String>> langOfPriority : priorityLangs.entrySet()) {
            for (String string : langOfPriority.getValue()) {
                if (string.length() == 2) {
                    // LANG
                    if (localLang == null && AvailableLocales.getAvailableLangs().containsKey(string)) {
                        localLang = string;
                    }
                } else if (string.length() == 5) {
                    // LANG - COUNTRY
                    String langKey = string.substring(0, 2);
                    if (localLang == null && AvailableLocales.getAvailableLangs().containsKey(langKey)) {
                        localLang = langKey;
                    }
                    String countryKey = string.substring(3, 5).toUpperCase();
                    if (localCountry == null && AvailableLocales.getAvailableLangs().containsKey(countryKey)) {
                        localCountry = countryKey;
                    }
                }
                if (localLang != null && localCountry != null) {
                    return new Locale(localLang, localCountry);
                }
            }

        }
        if (localLang == null) {
            localLang = HttpHeader.DEFAULT_LANG;
        }

        if (localCountry != null) {
            return new Locale(localLang, localCountry);
        }
        return new Locale(localLang, AvailableLocales.getDefaultCountry(localLang));
    }

    private void fillUpPriorityLands(String accepted) {
        // Split the accepted language in a list of locales order by
        // priority
        String[] langs = accepted.split(",");
        for (String lang : langs) {
            lang = lang.trim();
            if (lang.length() >= 2) {
                String[] splited = lang.split(";");
                if (splited.length == 1) {
                    addALang(splited[0].trim(), 1.F);
                } else if (splited.length == 2) {
                    try {
                        String wstr = splited[1].trim();
                        if (wstr.length() > 3) {
                            float w = Float.valueOf(wstr.substring(2));
                            addALang(splited[0].trim(), w);
                        }
                    } catch (NumberFormatException e) {
                        Log.framework().info("Malformed accepedLanguage", e);
                    }
                }
            }
        }
        if (priorityLangs.isEmpty()) {
            addALang("en", 0.f);
        }
    }

    private void addALang(String lang, float weight) {
        if (weight <= 1) {
            Integer priority = 100 - Math.round(weight * 100.F);
            List<String> langList = priorityLangs.get(priority);
            if (langList != null) {
                langList.add(lang);
            } else {
                List<String> list = new ArrayList<String>();
                list.add(lang);
                priorityLangs.put(priority, list);
            }
        }
    }

}
