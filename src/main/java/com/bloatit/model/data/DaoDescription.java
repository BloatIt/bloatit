package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

@Entity
public class DaoDescription extends DaoIdentifiable {

    private Locale defaultLocale;

    @OneToMany(mappedBy = "description")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Set<DaoTranslation> translations = new HashSet<DaoTranslation>(0);

    protected DaoDescription() {
        super();
    }

    static public DaoDescription createAndPersist(DaoMember member, Locale locale, String title, String description) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoDescription descr = new DaoDescription(member, locale, title, description);
        try {
            session.save(descr);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return descr;
    }

    public DaoDescription(DaoMember member, Locale locale, String title, String description) {
        super();
        setDefaultLocale(locale);
        this.translations.add(new DaoTranslation(member, this, locale, title, description));
    }

    public PageIterable<DaoTranslation> getTranslationsFromQuery() {
        QueryCollection<DaoTranslation> q = new QueryCollection<DaoTranslation>(
                "from com.bloatit.model.data.DaoTransaltion as t where t.description = :this");
        q.setEntity("this", this);
        return q;
    }

    public Set<DaoTranslation> getTranslations() {
        return translations;
    }

    public void addTranslation(DaoTranslation translation) {
        translations.add(translation);
    }

    public DaoTranslation getTranslation(Locale locale) {
        final Query q = SessionManager
                .getSessionFactory()
                .getCurrentSession()
                .createQuery("from com.bloatit.model.data.DaoTranslation as t where t.locale = :locale and t.description = :this");
        q.setLocale("locale", locale);
        q.setEntity("this", this);
        return (DaoTranslation) q.uniqueResult();
    }

    public DaoTranslation getDefaultTranslation() {
        return getTranslation(getDefaultLocale());
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
