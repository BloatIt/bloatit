package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.data.util.SessionManger;

@NamedQuery(name = "translation.getTextByLocal", query = "select text from Translation as t where t.locale = :locale")
@Entity
public class Translatable extends Identifiable {

    private Locale defaultLocale;

    @OneToMany(mappedBy = "baseText")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Set<Translation> translations = new HashSet<Translation>(0);

    // For Hibernate revers mapping.
    @ManyToOne
    private Demand demand;

    protected Translatable() {
        super();
    }

    public Translatable(Member member, Locale locale, String text) {
        super();
        setDefaultLocale(locale);
        this.translations.add(new Translation(member, locale, text));
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

    protected void setDemand(Demand demand) {
        this.demand = demand;
    }

    protected Demand getDemand() {
        return demand;
    }

}
