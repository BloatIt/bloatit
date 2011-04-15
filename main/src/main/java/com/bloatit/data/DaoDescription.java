//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.bloatit.framework.utils.PageIterable;

/**
 * A description is a localized text with a title. In fact a the data are stored
 * in daoTranslation. The description is a way of accessing different
 * translation. You can see a DaoTranslation as a version of a description is a
 * specific locale.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           cacheable=true,
                           name = "description.getTranslations.byLocale",
                           query = "FROM DaoTranslation WHERE locale = :locale AND description = :this")
                       }
             )
// @formatter:on
public class DaoDescription extends DaoIdentifiable {

    // @Field(index = Index.UN_TOKENIZED)
    private Locale defaultLocale;

    /**
     * This is a set of translation of this description
     */
    @OneToMany(mappedBy = "description")
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoTranslation> translations = new ArrayList<DaoTranslation>(0);

    public static DaoDescription createAndPersist(final DaoMember member,
                                                  final DaoTeam team,
                                                  final Locale locale,
                                                  final String title,
                                                  final String description) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoDescription descr = new DaoDescription(member, team, locale, title, description);
        try {
            session.save(descr);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
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
     * @param description is the main text of the description (the actual
     *            description)
     */
    private DaoDescription(final DaoMember member, final DaoTeam team, final Locale locale, final String title, final String description) {
        super();
        setDefaultLocale(locale);
        this.translations.add(new DaoTranslation(member, team, this, locale, title, description));
    }

    /**
     * Add a new translation to this description.
     */
    public void addTranslation(final DaoTranslation translation) {
        this.translations.add(translation);
    }

    /**
     * Change the default locale.
     */
    public void setDefaultLocale(final Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    // ======================================================================
    // Getters.
    // ======================================================================

    /**
     * @return the default translation for this description (using default
     *         locale)
     */
    public DaoTranslation getDefaultTranslation() {
        return getTranslation(getDefaultLocale());
    }

    /**
     * Gets the Translations of this description in a PageIterable This will
     * return every translation EVEN this description.
     */
    public PageIterable<DaoTranslation> getTranslations() {
        return new MappedList<DaoTranslation>(this.translations);
    }

    /**
     * Get a translation for a given locale.
     * 
     * @param locale the locale in which we want the description
     * @return null if no translation exists for this locale.
     */
    public DaoTranslation getTranslation(final Locale locale) {
        final Query q = SessionManager.getNamedQuery("description.getTranslations.byLocale");
        q.setLocale("locale", locale);
        q.setEntity("this", this);
        return (DaoTranslation) q.uniqueResult();
    }

    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoDescription() {
        super();
    }

    // ======================================================================
    // equals hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.defaultLocale == null) ? 0 : this.defaultLocale.hashCode());
        result = prime * result + ((this.translations == null) ? 0 : this.translations.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DaoDescription)) {
            return false;
        }
        final DaoDescription other = (DaoDescription) obj;
        if (this.defaultLocale == null) {
            if (other.defaultLocale != null) {
                return false;
            }
        } else if (!this.defaultLocale.equals(other.defaultLocale)) {
            return false;
        }
        if (this.translations == null) {
            if (other.translations != null) {
                return false;
            }
        } else if (!this.translations.equals(other.translations)) {
            return false;
        }
        return true;
    }

}
