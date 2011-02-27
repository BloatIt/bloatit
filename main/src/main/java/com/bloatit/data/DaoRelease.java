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

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoRelease extends DaoUserContent implements DaoCommentable {
    @Basic(optional = false)
    private String description;

    /**
     * This is the language in which the description is written.
     */
    @Basic(optional = false)
    private Locale locale;

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    @OrderBy("id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<DaoComment> comments = new ArrayList<DaoComment>();

    @ManyToOne(optional = false)
    @Column(updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoBatch batch;

    private String version;

    public DaoRelease(final DaoMember member, final DaoBatch batch, final String description, final String version, final Locale locale) {
        super(member);
        if (description == null || batch == null || locale == null || version == null || description.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.batch = batch;
        this.description = description;
        this.locale = locale;
        this.version = version;
    }

    public static DaoRelease createAndPersist(final DaoMember member,
                                              final DaoBatch batch,
                                              final String description,
                                              final String version,
                                              final Locale locale) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoRelease release = new DaoRelease(member, batch, description, version, locale);
        try {
            session.save(release);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return release;
    }

    @Override
    public void addComment(final DaoComment comment) {
        this.comments.add(comment);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    public String getVersion() {
        return this.version;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return this.locale;
    }

    public DaoBatch getBatch() {
        return this.batch;
    }

    /**
     * @return the comments
     */
    @Override
    public PageIterable<DaoComment> getComments() {
        return new MappedList<DaoComment>(this.comments);
    }

    /**
     * @return the last comment
     */
    @Override
    public DaoComment getLastComment() {
        return this.comments.get(this.comments.size() - 1);
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // Hibernate mapping
    // ======================================================================

    protected DaoRelease() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoRelease other = (DaoRelease) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        return true;
    }
}
