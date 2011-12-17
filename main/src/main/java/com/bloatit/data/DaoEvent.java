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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = {
          @NamedQuery(
                      name =  "memberid.event.byDate.withMail",
                      query = "SELECT mff.id, e " +
                              "FROM DaoEvent e " +
                              "JOIN e.feature f " +
                              "LEFT JOIN f.followers mf " +
                              "LEFT JOIN mf.follower mff " +
                              "WHERE e.creationDate > :beginDate " +
                              "AND e.creationDate <= :endDate " +
                              "AND (mff.emailStrategy = :strategy OR mff is null) " +
                              "AND (mf.mail = true OR mf is null) " +
                              "ORDER BY mff.id, e.creationDate "
                              ),
          @NamedQuery(
                      name =  "memberid.event.byDate.withMail.size",
                      query = "SELECT count(*) " +
                              "FROM DaoEvent e " +
                              "JOIN e.feature f " +
                              "LEFT JOIN f.followers mf " +
                              "LEFT JOIN mf.follower mff " +
                              "WHERE e.creationDate > :beginDate " +
                              "AND e.creationDate <= :endDate " +
                              "AND (mff.emailStrategy = :strategy OR mff is null) " +
                              "AND (mf.mail = true OR mf is null) "
                              ),
                              
          @NamedQuery(
                      name =  "event.byMember",
                      query = "SELECT e " +
                              "FROM DaoEvent e " +
                              "JOIN e.feature f " +
                              "LEFT JOIN f.followers mf " +
                              "LEFT JOIN mf.follower mff " +
                              "WHERE NOT (mf.featureComment=false AND e.isFeatureComment=true) " +
                              "AND NOT (mf.bugComment=false AND e.isBugComment=true) " +
                              "AND mff.id = :member "+
                              "ORDER BY mff.id, e.creationDate "
                              ),
          @NamedQuery(
                      name =  "event.byMember.size",
                      query = "SELECT count(*) " +
                              "FROM DaoEvent e " +
                              "JOIN e.feature f " +
                              "LEFT JOIN f.followers mf " +
                              "LEFT JOIN mf.follower mff " +
                              "WHERE NOT (mf.featureComment=false AND e.isFeatureComment=true) " +
                              "AND NOT (mf.bugComment=false AND e.isBugComment=true) " +
                              "AND mff.id = :member "
                              ),
          @NamedQuery(
                      name =  "event.getall",
                      query = "SELECT e " +
                              "FROM DaoEvent e " +
                              "JOIN e.feature f " +
                              "ORDER BY e.creationDate "
                              ),
          @NamedQuery(
                      name =  "event.getall.size",
                      query = "SELECT count(*) " +
                              "FROM DaoEvent e " +
                              "JOIN e.feature f "
                      ),
})
//@formatter:on
public class DaoEvent extends DaoIdentifiable {

    public enum EventType {

        // Feature
        CREATE_FEATURE, //
        IN_DEVELOPING_STATE, //
        DISCARDED, // TODO: remove every event from this feature.
        FINICHED, //

        // Contributions
        ADD_CONTRIBUTION, //
        REMOVE_CONTRIBUTION, //

        // Offer
        ADD_OFFER, //
        REMOVE_OFFER, //
        ADD_SELECTED_OFFER, //
        CHANGE_SELECTED_OFFER, //
        REMOVE_SELECTED_OFFER, //

        // Release
        ADD_RELEASE, //

        // Bugs
        ADD_BUG, //
        BUG_CHANGE_LEVEL, //
        BUG_SET_RESOLVED, //
        BUG_SET_DEVELOPING, //

        // Comment
        FEATURE_ADD_COMMENT, //
        BUG_ADD_COMMENT, //
    }

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Basic(optional = false)
    private boolean isBugComment;
    @Basic(optional = false)
    private boolean isFeatureComment;

    @Basic(optional = false)
    private Date creationDate;

    @ManyToOne(optional = false)
    private DaoFeature feature;
    @ManyToOne(optional = false)
    private DaoActor actor;
    @ManyToOne
    private DaoSoftware software;

    @ManyToOne
    private DaoContribution contribution;
    @ManyToOne(cascade = { CascadeType.ALL })
    private DaoOffer offer;
    @ManyToOne
    private DaoComment comment;
    @ManyToOne
    private DaoBug bug;
    @ManyToOne
    private DaoRelease release;
    @ManyToOne
    private DaoMilestone milestone;

    public static PageIterable<DaoEvent> getEvent(final DaoMember member, final Date fromDate) {
        return new QueryCollection<DaoEvent>("event.byMemberDate");
    }

    public static PageIterable<DaoEvent> getMailEvent(final DaoMember member, final Date fromDate) {
        return new QueryCollection<DaoEvent>("event.byMemberDate.withMail");
    }

    private static DaoEvent createAndPersist(final EventType type,
                                             final DaoFeature feature,
                                             final DaoContribution contribution,
                                             final DaoOffer offer,
                                             final DaoComment comment,
                                             final DaoBug bug,
                                             final DaoRelease release,
                                             final DaoMilestone milestone) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoEvent event = new DaoEvent(type, feature, contribution, offer, comment, bug, release, milestone);
        try {
            session.save(event);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return event;
    }

    public static DaoEvent createFeatureEvent(final DaoFeature feature, final EventType type) {
        return (createAndPersist(type, feature, null, null, null, null, null, null));
    }

    public static DaoEvent createContributionEvent(final DaoFeature feature, final EventType type, final DaoContribution contribution) {
        return (createAndPersist(type, feature, contribution, null, null, null, null, null));
    }

    public static DaoEvent createOfferEvent(final DaoFeature feature, final EventType type, final DaoOffer offer) {
        return (createAndPersist(type, feature, null, offer, null, null, null, null));
    }

    public static DaoEvent createCommentEvent(final DaoFeature feature, final EventType type, final DaoComment comment) {
        return (createAndPersist(type, feature, null, null, comment, null, null, null));
    }

    public static DaoEvent createCommentEvent(final DaoFeature feature,
                                              final EventType type,
                                              final DaoBug bug,
                                              final DaoComment comment,
                                              final DaoOffer offer,
                                              final DaoMilestone milestone) {
        return (createAndPersist(type, feature, null, offer, comment, bug, null, milestone));
    }

    public static DaoEvent createBugEvent(final DaoFeature feature,
                                          final EventType type,
                                          final DaoBug bug,
                                          final DaoOffer offer,
                                          final DaoMilestone milestone) {
        return (createAndPersist(type, feature, null, offer, null, bug, null, milestone));
    }

    public static DaoEvent createReleaseEvent(final DaoFeature feature,
                                              final EventType type,
                                              final DaoRelease release,
                                              final DaoOffer offer,
                                              final DaoMilestone milestone) {
        return (createAndPersist(type, feature, null, offer, null, null, release, milestone));
    }

    public static DaoEvent createReleaseCommentEvent(final DaoFeature feature,
                                                     final EventType type,
                                                     final DaoComment comment,
                                                     final DaoRelease release,
                                                     final DaoOffer offer,
                                                     final DaoMilestone milestone) {
        return (createAndPersist(type, feature, null, offer, comment, null, release, milestone));
    }

    private DaoEvent(final EventType type,
                     final DaoFeature feature,
                     final DaoContribution contribution,
                     final DaoOffer offer,
                     final DaoComment comment,
                     final DaoBug bug,
                     final DaoRelease release,
                     final DaoMilestone milestone) {
        super();
        if (type == null || feature == null) {
            throw new NonOptionalParameterException();
        }
        this.feature = feature;
        this.actor = feature.getAuthor();
        this.software = feature.getSoftware();
        this.creationDate = new Date();
        this.isFeatureComment = release == null && bug == null && comment != null;
        this.isBugComment = bug != null && comment != null;
        this.type = type;
        this.contribution = contribution;
        this.offer = offer;
        this.comment = comment;
        this.bug = bug;
        this.release = release;
        this.milestone = milestone;
    }

    public EventType getType() {
        return type;
    }

    public boolean isBugComment() {
        return isBugComment;
    }

    public boolean isFeatureComment() {
        return isFeatureComment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public DaoFeature getFeature() {
        return feature;
    }

    public DaoContribution getContribution() {
        return contribution;
    }

    public DaoOffer getOffer() {
        return offer;
    }

    public DaoComment getComment() {
        return comment;
    }

    public DaoBug getBug() {
        return bug;
    }

    public DaoRelease getRelease() {
        return release;
    }

    public DaoMilestone getMilestone() {
        return milestone;
    }

    public DaoActor getActor() {
        return actor;
    }

    public DaoSoftware getSoftware() {
        return software;
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
    // Hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao bug.
     */
    protected DaoEvent() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bug == null) ? 0 : bug.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((contribution == null) ? 0 : contribution.hashCode());
        result = prime * result + ((feature == null) ? 0 : feature.hashCode());
        result = prime * result + (isBugComment ? 1231 : 1237);
        result = prime * result + (isFeatureComment ? 1231 : 1237);
        result = prime * result + ((milestone == null) ? 0 : milestone.hashCode());
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((release == null) ? 0 : release.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

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
        final DaoEvent other = (DaoEvent) obj;
        if (bug == null) {
            if (other.bug != null) {
                return false;
            }
        } else if (!bug.equals(other.bug)) {
            return false;
        }
        if (comment == null) {
            if (other.comment != null) {
                return false;
            }
        } else if (!comment.equals(other.comment)) {
            return false;
        }
        if (contribution == null) {
            if (other.contribution != null) {
                return false;
            }
        } else if (!contribution.equals(other.contribution)) {
            return false;
        }
        if (feature == null) {
            if (other.feature != null) {
                return false;
            }
        } else if (!feature.equals(other.feature)) {
            return false;
        }
        if (isBugComment != other.isBugComment) {
            return false;
        }
        if (isFeatureComment != other.isFeatureComment) {
            return false;
        }
        if (milestone == null) {
            if (other.milestone != null) {
                return false;
            }
        } else if (!milestone.equals(other.milestone)) {
            return false;
        }
        if (offer == null) {
            if (other.offer != null) {
                return false;
            }
        } else if (!offer.equals(other.offer)) {
            return false;
        }
        if (release == null) {
            if (other.release != null) {
                return false;
            }
        } else if (!release.equals(other.release)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }
}
