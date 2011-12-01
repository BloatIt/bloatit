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
public class DaoFollowFeature extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMember follower;
    @ManyToOne(optional = false)
    private DaoFeature followed;

    @Basic(optional = false)
    private boolean featureComment;
    @Basic(optional = false)
    private boolean bugComment;

    @Basic(optional = false)
    private boolean mail;

    // ======================================================================
    // Construct.
    // ======================================================================

    public static DaoFollowFeature createAndPersist(DaoMember follower,
                                                    DaoFeature followed,
                                                    boolean featureComment,
                                                    boolean bugComment,
                                                    boolean mail) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoFollowFeature feature = new DaoFollowFeature(follower, followed, featureComment, bugComment, mail);
        try {
            session.save(feature);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return feature;
    }

    public DaoFollowFeature(DaoMember follower, DaoFeature followed, boolean featureComment, boolean bugComment, boolean mail) {
        super();
        this.follower = follower;
        this.followed = followed;
        this.featureComment = featureComment;
        this.bugComment = bugComment;
        this.mail = mail;
    }

    public void setFeatureComment(boolean featureComment) {
        this.featureComment = featureComment;
    }

    public void setBugComment(boolean bugComment) {
        this.bugComment = bugComment;
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

    public DaoFeature getFollowed() {
        return followed;
    }

    public boolean isFeatureComment() {
        return featureComment;
    }

    public boolean isBugComment() {
        return bugComment;
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
        return null;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao feature.
     */
    protected DaoFollowFeature() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (bugComment ? 1231 : 1237);
        result = prime * result + (featureComment ? 1231 : 1237);
        result = prime * result + ((followed == null) ? 0 : followed.hashCode());
        result = prime * result + ((follower == null) ? 0 : follower.hashCode());
        result = prime * result + (mail ? 1231 : 1237);
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
        DaoFollowFeature other = (DaoFollowFeature) obj;
        if (bugComment != other.bugComment)
            return false;
        if (featureComment != other.featureComment)
            return false;
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
        if (mail != other.mail)
            return false;
        return true;
    }

    
}
