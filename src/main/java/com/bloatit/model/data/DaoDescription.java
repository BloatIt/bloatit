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

@NamedQuery(name = "translation.getTextByLocal", query = "from DaoTranslation as t where t.locale = :locale")
@Entity
public class DaoDescription extends DaoIdentifiable {

    private Locale defaultLocale;

    @OneToMany(mappedBy = "baseText")
    private Set<DaoTranslation> translations = new HashSet<DaoTranslation>(0);

    protected DaoDescription() {
        super();
    }
    
    static public DaoDescription createAndPersist(DaoMember member, Locale locale, String title, String description){
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        DaoDescription descr = new DaoDescription(member, locale, title, description);
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

    public DaoDescription(DaoMember member, Locale locale, String title, String description) {
        super();
        setDefaultLocale(locale);
        this.translations.add(new DaoTranslation(member, locale, title, description));
    }

    public Set<DaoTranslation> getTranslations() {
        return translations;
    }

    public DaoTranslation getDefaultTranslation() {
        return (DaoTranslation) SessionManger.getSessionFactory().getCurrentSession().getNamedQuery("translation.getTextByLocal").uniqueResult();
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

    protected void setTranslations(Set<DaoTranslation> Translations) {
        this.translations = Translations;
    }
}
