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
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

@Entity
public class DaoFollowSoftware extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMember follower;
    @ManyToOne(optional = false)
    private DaoSoftware followed;

    @Basic(optional = false)
    private boolean mail;

    // ======================================================================
    // Construct.
    // ======================================================================

    public static DaoFollowSoftware createAndPersist(DaoMember follower, DaoSoftware followed, boolean mail) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoFollowSoftware feature = new DaoFollowSoftware(follower, followed, mail);
        try {
            session.save(feature);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return feature;
    }

    public DaoFollowSoftware(DaoMember follower, DaoSoftware followed, boolean mail) {
        super();
        this.follower = follower;
        this.followed = followed;
        this.mail = mail;
    }

    public void setMail(boolean mail) {
        this.mail = mail;
    }
    
    public void unfollow() {
        SessionManager.getSessionFactory().getCurrentSession().delete(this);
    }

    // ======================================================================
    // Getters.
    // ======================================================================

    public DaoMember getFollower() {
        return follower;
    }

    public DaoSoftware getFollowed() {
        return followed;
    }

    public boolean isMail() {
        return mail;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoIdentifiable#accept(com.bloatit.data.DataClassVisitor
     * )
     */
    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao feature.
     */
    protected DaoFollowSoftware() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((followed == null) ? 0 : followed.hashCode());
        result = prime * result + ((follower == null) ? 0 : follower.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DaoFollowSoftware other = (DaoFollowSoftware) obj;
        if (followed == null) {
            if (other.followed != null)
                return false;
        } else if (!followed.equals(other.followed))
            return false;
        if (follower == null) {
            if (other.follower != null)
                return false;
        } else if (!follower.equals(other.follower))
            return false;
        return true;
    }

}
