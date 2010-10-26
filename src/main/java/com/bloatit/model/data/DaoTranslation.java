package com.bloatit.model.data;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

// This class should handle some versions.
// version are managed by date.
@Entity
public class DaoTranslation extends DaoKudosable {

    @Basic(optional = false)
    private Locale locale;
    @Basic(optional = false)
    private String title;
    @Basic(optional = false)
    private String description;

    @ManyToOne(optional = false)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private DaoDescription baseText;

    protected DaoTranslation() {}

    public DaoTranslation(DaoActor Actor, Locale locale, String title, String description) {
        super(Actor);
        this.locale = locale;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    protected void setBaseText(DaoDescription baseText) {
        this.baseText = baseText;
    }

    protected DaoDescription getBaseText() {
        return baseText;
    }
}
