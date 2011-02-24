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

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * A kudos is a positive or negative appreciation of a Kudosable content. [ Yes
 * there is a 's' at the end of kudos even when there is only one. ] The kudos
 * is an internal storing class. You should never have to use it in other
 * package.
 *
 * @see DaoKudosable#addKudos(DaoMember, int)
 */
@Entity
public final class DaoKudos extends DaoUserContent {

    /**
     * The value can be positive or negative. The value should never be equals
     * zero.
     */
    @Basic(optional = false)
    private int value;

    protected static DaoKudos createAndPersist(final DaoMember member, final int value) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoKudos kudos = new DaoKudos(member, value);
        try {
            session.save(kudos);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return kudos;
    }

    /**
     * Create a new kudos.
     *
     * @param member is the person creating the kudos.
     * @param value is value of the kudos.
     */
    public DaoKudos(final DaoMember member, final int value) {
        super(member);
        this.value = value;
    }

    public int getValue() {
        return value;
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

    protected DaoKudos() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + value;
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoKudos other = (DaoKudos) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

}
