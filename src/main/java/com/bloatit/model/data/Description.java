package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.data.util.SessionManger;

@NamedQuery(name = "translation.getTextByLocal", query = "from Translation as t where t.locale = :locale")
@Entity
public class Description extends Identifiable {

    private Locale defaultLocale;

    @OneToMany(mappedBy = "baseText")
    private Set<Translation> translations = new HashSet<Translation>(0);

    protected Description() {
        super();
    }

    public Description(Member member, Locale locale, String title, String description) {
        super();
        setDefaultLocale(locale);
        this.translations.add(new Translation(member, locale, title, description));
    }

    public Set<Translation> getTranslations() {
        return translations;
    }

    public Translation getDefaultTranslation() {
        return (Translation) SessionManger.getSessionFactory().getCurrentSession().getNamedQuery("translation.getTextByLocal").uniqueResult();
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setTranslations(Set<Translation> translations) {
        this.translations = translations;
    }
}
