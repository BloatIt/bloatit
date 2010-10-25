package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Session;
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
    
    static public Description createAndPersist(Member member, Locale locale, String title, String description){
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Description descr = new Description(member, locale, title, description);
        try {
            session.save(descr);
        } catch (HibernateException e) {
            System.out.println(e);
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return descr;
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
