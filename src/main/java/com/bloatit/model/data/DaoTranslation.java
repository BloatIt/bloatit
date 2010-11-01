package com.bloatit.model.data;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

// This class should handle some versions.
// version are managed by date.
@Entity
public class DaoTranslation extends DaoKudosable {

    @Basic(optional = false)
    private Locale locale;
    @Basic(optional = false)
    private String title;
    @Basic(optional = false)
    private String text;

    @ManyToOne(optional = false)
    private DaoDescription description;

    protected DaoTranslation() {}

    public DaoTranslation(DaoMember member, DaoDescription description, Locale locale, String title, String text) {
        super(member);
        this.locale = locale;
        this.title = title;
        this.text = text;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getText() {
        return text;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setLocale(Locale locale) {
        this.locale = locale;
    }

    protected void setText(String text) {
        this.text = text;
    }

    protected DaoDescription getDescription() {
        return description;
    }

    protected void setDescription(DaoDescription description) {
        this.description = description;
    }

}
