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

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.data.DaoEvent.EventType;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * A Release is a finished version of an implemented feature. There should be at
 * lease one release by milestone.
 * 
 * @author Thomas Guyard
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoRelease extends DaoUserContent {
    @Basic(optional = false)
    private String description;

    /**
     * This is the language in which the description is written.
     */
    @Basic(optional = false)
    private Locale locale;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoMilestone milestone;

    private String version;

    private DaoRelease(final DaoMember member,
                       final DaoTeam team,
                       final DaoMilestone milestone,
                       final String description,
                       final String version,
                       final Locale locale) {
        super(member, team);
        if (description == null || milestone == null || locale == null || version == null || description.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.milestone = milestone;
        this.description = description;
        this.locale = locale;
        this.version = version;
    }

    /**
     * @param member the author of the release.
     * @param team the as team property of this release.
     * @param milestone the milestone on which this release has been done
     * @param description the description of the release
     * @param version the version of the release (for example 1.2.3 )
     * @param locale the language in which the description has been written.
     * @return the new release
     */
    public static DaoRelease createAndPersist(final DaoMember member,
                                              final DaoTeam team,
                                              final DaoMilestone milestone,
                                              final String description,
                                              final String version,
                                              final Locale locale) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoRelease release = new DaoRelease(member, team, milestone, description, version, locale);
        try {
            session.save(release);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        DaoEvent.createReleaseEvent(release.getMilestone().getOffer().getFeature(), EventType.ADD_RELEASE, release, milestone.getOffer(), milestone);
        return release;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the version of this release.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * @return the milestone on which this release has been added.
     */
    public DaoMilestone getMilestone() {
        return this.milestone;
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
