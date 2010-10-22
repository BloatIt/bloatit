package com.bloatit.model.data;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

// This class should handle some versions.
// version are managed by date.
@Entity
public class Translation extends Kudosable {

	@Basic(optional = false)
	private Locale locale;
	@Basic(optional = false)
	private String text;
	
	@ManyToOne(optional = false)
	private LocalizedText baseText;

	protected Translation() {
	}

	public Translation(Member member, Locale locale, String text) {
		super(member);
		this.locale = locale;
		this.text = text;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	protected void setBaseText(LocalizedText baseText) {
	    this.baseText = baseText;
    }

	protected LocalizedText getBaseText() {
	    return baseText;
    }
}
