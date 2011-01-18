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
import org.hibernate.search.annotations.IndexedEmbedded;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

/**
 * A description is a localized text with a title. In fact a the data are stored in
 * daoTranslation. The description is a way of accessing different translation. You can
 * see a DaoTranslation as a version of a description is a specific locale.
 */
@Entity
public final class DaoDescription extends DaoIdentifiable {

    // @Field(index = Index.UN_TOKENIZED)
    private Locale defaultLocale;

    /**
     * This is a set of translation of this description
     */
    @OneToMany(mappedBy = "description")
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private final Set<DaoTranslation> translations = new HashSet<DaoTranslation>(0);

    public static DaoDescription createAndPersist(final DaoMember member, final Locale locale, final String title, final String description) {
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

    /**
     * Create a daoDescription. Set the default locale to "locale"
     *
     * @param member is the author of this description
     * @param locale is the locale in which the description is written.
     * @param title is the title of the description
     * @param description is the main text of the description (the actual description)
     */
    private DaoDescription(final DaoMember member, final Locale locale, final String title, final String description) {
        super();
        setDefaultLocale(locale);
        translations.add(new DaoTranslation(member, this, locale, title, description));
    }

    /**
     * Use a HQL query to get the Translations of this description in a PageIterable This
     * will return every translation EVEN this description.
     */
    public PageIterable<DaoTranslation> getTranslations() {
        return new QueryCollection<DaoTranslation>("from DaoTransaltion as t where t.description = :this").setEntity("this", this);
    }

    /**
     * Add a new translation to this description.
     */
    public void addTranslation(final DaoTranslation translation) {
        translations.add(translation);
    }

    /**
     * Get a translation for a given locale.
     *
     * @param locale the locale in which we want the description
     * @return null if no translation exists for this locale.
     */
    public DaoTranslation getTranslation(final Locale locale) {
        final Query q = SessionManager
                .createQuery("from com.bloatit.model.data.DaoTranslation as t where t.locale = :locale and t.description = :this");
        q.setLocale("locale", locale);
        q.setEntity("this", this);
        return (DaoTranslation) q.uniqueResult();
    }

    /**
     * @return the default translation for this description (using default locale)
     */
    public DaoTranslation getDefaultTranslation() {
        return getTranslation(getDefaultLocale());
    }

    /**
     * Change the default locale.
     */
    public void setDefaultLocale(final Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoDescription() {
        super();
    }
}
