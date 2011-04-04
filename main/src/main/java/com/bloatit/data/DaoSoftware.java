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

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.OrderBy;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "software.byName",
                           query = "FROM DaoSoftware WHERE name = :name"),
                       }
             )
// @formatter:on
public class DaoSoftware extends DaoIdentifiable {

    @Column(nullable = false, unique = true, updatable = false)
    private String name;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoDescription description;

    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy(clause = "id")
    private DaoFileMetadata image;

    @OneToMany(mappedBy = "software")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private final List<DaoFeature> features = new ArrayList<DaoFeature>();

    // ======================================================================
    // Static HQL requests
    // ======================================================================

    public static DaoSoftware getByName(final String name) {
        final Query query = SessionManager.getNamedQuery("software.byName").setString("name", name);
        return (DaoSoftware) query.uniqueResult();
    }

    // ======================================================================
    // Construction
    // ======================================================================

    public static DaoSoftware createAndPersist(final String name, final DaoDescription description) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoSoftware software = new DaoSoftware(name, description);
        try {
            session.save(software);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return software;
    }

    private DaoSoftware(final String name, final DaoDescription description) {
        super();
        if (name == null || name.isEmpty() || description == null) {
            throw new NonOptionalParameterException();
        }
        this.name = name;
        this.description = description;
    }

    protected void addFeature(final DaoFeature feature) {
        this.features.add(feature);
    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * @return the description
     */
    public DaoDescription getDescription() {
        return this.description;
    }

    /**
     * @return the image
     */
    public DaoFileMetadata getImage() {
        return this.image;
    }

    public PageIterable<DaoFeature> getFeatures() {
        return new MappedList<DaoFeature>(this.features);
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For Hibernate mapping.
    // ======================================================================

    protected DaoSoftware() {
        super();
    }

    // ======================================================================
    // equals and hashcode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoSoftware other = (DaoSoftware) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }

        return true;
    }

    public void setImage(final DaoFileMetadata image) {
        this.image = image;
    }

}
