package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.util.HibernateUtil;

/**
 * A localized text is not a UserContent, because it is only a container class.
 * The Demand, comment etc. are UserContent. We can imagine to have a Localized
 * Text not created by a user.
 * 
 * The localText/Translation tables are far from being correct (BC normalized).
 * I should have something like: {@code text(*id, string, id_locale)}
 * {@code local(*id, * localeCode)}
 * {@code translation(*id, string) text_locale(*id_text, *id_local, * id_translation)}
 * This solution add a lot of "join". (the localized text is used in every
 * translatable text ...)
 * 
 */
//@NamedQuery(name = "translation.getTextByLocale", query = "select text from Translation as t where t.locale = :locale")
@Entity
public class LocalizedText extends Identifiable {

	private Locale locale;
	private String text;

	@OneToMany(mappedBy = "baseText")
	private Set<Translation> translations = new HashSet<Translation>(0);

	protected LocalizedText() {
		super();
	}

	public LocalizedText(Locale locale, String text) {
		super();
		this.locale = locale;
		this.text = text;
	}

	public boolean hasText(Locale locale) {
		if (this.locale == locale) {
			return true;
		}
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("translation.getTextByLocale");
		q.setLocale("locale", locale);
		return (q.list().size() > 0);
	}

	public Locale defaultLocale() {
		return locale;
	}

	public Locale whichLocale(Locale locale) {
		if (hasText(locale)) {
			return locale;
		}
		return defaultLocale();
	}

	public String getText(Locale locale) {
		if (this.locale == locale) {
			return text;
		}
		return findTextInTranslation(locale);
	}

	private String findTextInTranslation(Locale locale) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Query q = session.getNamedQuery("translation.getTextByLocale");
			q.setLocale("locale", locale);
			if (q.list().size() > 0) {
				return (String) q.list().get(0);
			}
		} catch (HibernateException e) {
			return text;
		}
		return text;
	}
	
	public Set<Translation> getTranslations() {
		return translations;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected Locale getLocale() {
		return locale;
	}

	protected void setLocale(Locale locale) {
		this.locale = locale;
	}

	protected void setText(String text) {
		this.text = text;
	}

	protected String getText() {
		return text;
	}

	protected void setTranslations(Set<Translation> translations) {
	    this.translations = translations;
    }

}
