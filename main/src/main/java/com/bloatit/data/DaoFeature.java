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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.OrderBy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.bloatit.common.Log;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.data.queries.EmptyPageIterable;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.data.search.DaoFeatureSearchFilterFactory;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Contribution;

/**
 * A DaoFeature is a kudosable content. It has a translatable description, and
 * can have a specification and some offers. The state of the feature is managed
 * by its super class DaoKudosable. On a feature we can add some comment and
 * some contributions.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed
@FullTextFilterDef(name = "searchFilter", impl = DaoFeatureSearchFilterFactory.class)
//@formatter:off
@NamedQueries(value = {@NamedQuery(
                           name = "feature.getOffers.bySelected",
                           query = "FROM DaoOffer " +
                                   "WHERE feature = :this " +
                                   "AND state <= :state " + // <= PENDING and VALIDATED.
                                   "AND popularity = (select max(popularity) from DaoOffer where feature = :this) " + //
                                   "AND popularity >= 0 " +
                                   "ORDER BY amount ASC, creationDate DESC"),

                        @NamedQuery(
                           name = "feature.getAmounts.min",
                           query = "SELECT min(amount) FROM DaoContribution WHERE feature = :this"),

                        @NamedQuery(
                           name = "feature.getAmounts.max",
                           query = "SELECT max(amount) FROM DaoContribution WHERE feature = :this"),

                       @NamedQuery(
                           name = "feature.getAmounts.avg",
                           query = "SELECT avg(amount) FROM DaoContribution WHERE feature = :this"),

                        @NamedQuery(
                            name = "feature.getBugs.byNonState",
                            query = "SELECT bugs_ " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "JOIN bs.bugs as bugs_ " +
                                    "WHERE offer_ = :offer " +
                                    "AND bugs_.state != :state "),

                        @NamedQuery(
                            name = "feature.getBugs.byNonState.size",
                            query = "SELECT count(bugs_) " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "JOIN bs.bugs as bugs_ " +
                                    "WHERE offer_ = :offer " +
                                    "AND bugs_.state != :state "),

                        @NamedQuery(
                            name = "feature.getBugs.byState",
                            query = "SELECT bugs_ " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "JOIN bs.bugs as bugs_ " +
                                    "WHERE offer_ = :offer " +
                                    "AND bugs_.state = :state "),

                        @NamedQuery(
                            name = "feature.getBugs.byState.size",
                            query = "SELECT count(bugs_) " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "JOIN bs.bugs as bugs_ " +
                                    "WHERE offer_ = :offer " +
                                    "AND bugs_.state = :state ",
                            readOnly = true),
                        @NamedQuery(
                            name = "feature.getComments.size",
                            query = "SELECT count(*) "+
                                    "FROM com.bloatit.data.DaoComment  "+
                                    "WHERE feature = :this "+
                                    "OR father.id in ( "+
                                        "FROM com.bloatit.data.DaoComment  "+
                                        "WHERE feature = :this )",
                            readOnly = true),
                        @NamedQuery(
                            name = "feature.getContributionOf",
                            query = "SELECT sum(amount) "+
                                    "FROM com.bloatit.data.DaoContribution as c "+
                                    "WHERE c.feature = :this "+
                                    "AND c.member = :member " +
                                    "AND c.asTeam = null "),
                        @NamedQuery(
                             name="feature.getContribution.canceled", 
                             query="FROM com.bloatit.data.DaoContribution as c " +
                             		"WHERE c.feature = :this " +
                             		"AND c.state = :state "),
                        @NamedQuery(
                             name="feature.getContribution.notcanceled", 
                             query="FROM com.bloatit.data.DaoContribution as c " +
                                   "WHERE c.feature = :this " +
                                   "AND c.state != :state "),
                       @NamedQuery(
                             name="feature.getContribution.canceled.size", 
                             query="SELECT COUNT(*) " +
                             	   "FROM com.bloatit.data.DaoContribution as c " +
                                   "WHERE c.feature = :this " +
                                   "AND c.state = :state "),
                      @NamedQuery(
                             name="feature.getContribution.notcanceled.size", 
                             query="SELECT COUNT(*) " +
                             	   "FROM com.bloatit.data.DaoContribution as c " +
                                   "WHERE c.feature = :this " +
                                   "AND c.state != :state "),
                     }
             )
// @formatter:on
public class DaoFeature extends DaoKudosable implements DaoCommentable {

    /**
     * This is the state of the feature. It's used in the workflow modeling. The
     * order is important !
     */
    public enum FeatureState {

        /** No offers, waiting for money and offer. */
        PENDING,

        /** One or more offer, waiting for money. */
        PREPARING,

        /** Development in progress. */
        DEVELOPPING,

        /** Something went wrong, the feature is canceled. */
        DISCARDED,

        /** All is good, the developer is paid and the users are happy. */
        FINISHED
    }

    /**
     * This is a calculated value with the sum of the value of all
     * contributions.
     */
    @Basic(optional = false)
    @Field(store = Store.NO, index = Index.UN_TOKENIZED)
    private BigDecimal contribution;

    @Basic(optional = false)
    @Field
    @Enumerated
    private FeatureState featureState;

    /**
     * A description is a translatable text with an title.
     */
    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @IndexedEmbedded
    private DaoDescription description;

    @OneToMany(mappedBy = "feature")
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @IndexedEmbedded
    private final List<DaoOffer> offers = new ArrayList<DaoOffer>(0);

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private final List<DaoContribution> contributions = new ArrayList<DaoContribution>(0);

    @OneToMany(mappedBy = "feature")
    @OrderBy(clause = "id")
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @IndexedEmbedded
    private final List<DaoComment> comments = new ArrayList<DaoComment>();

    /**
     * The selected offer is the offer that is most likely to be validated and
     * used. If an offer is selected and has enough money and has a elapse time
     * done then this offer go into dev.
     */
    @ManyToOne(optional = true)
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @IndexedEmbedded
    private DaoOffer selectedOffer;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @IndexedEmbedded
    private DaoSoftware software;

    @Basic(optional = true)
    private Date validationDate;

    // ======================================================================
    // Construct.
    // ======================================================================

    /**
     * Creates the and persist.
     * 
     * @param member the author of the feature
     * @param team the as team property. Can be null.
     * @param description the description of the feature
     * @param software the software on which this feature has to be added
     * @return the new feature
     * @see #DaoFeature(DaoMember, DaoTeam, DaoDescription, DaoSoftware)
     */
    public static DaoFeature
            createAndPersist(final DaoMember member, final DaoTeam team, final DaoDescription description, final DaoSoftware software) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoFeature feature = new DaoFeature(member, team, description, software);
        try {
            session.save(feature);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return feature;
    }

    /**
     * Create a DaoFeature and set its state to the state PENDING.
     * 
     * @param member is the author of the feature
     * @param description is the description ...
     * @throws NonOptionalParameterException if any of the parameter is null.
     */
    private DaoFeature(final DaoMember member, final DaoTeam team, final DaoDescription description, final DaoSoftware software) {
        super(member, team);
        if (description == null) {
            throw new NonOptionalParameterException();
        }
        if (software != null) {
            this.software = software;
            software.addFeature(this);
        }
        this.description = description;
        this.validationDate = null;
        setSelectedOffer(null);
        this.contribution = BigDecimal.ZERO;
        setFeatureState(FeatureState.PENDING);
    }

    public void setSoftware(final DaoSoftware soft) {
        if (software != null) {
            this.software.removeFeature(this);
        }
        this.software = soft;
        software.addFeature(this);
    }

    /**
     * Delete this DaoFeature from the database. "this" will remain, but
     * unmapped. (You shoudn't use it then)
     */
    protected void delete() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoCommentable#addComment(com.bloatit.data.DaoComment)
     */
    @Override
    public void addComment(final DaoComment comment) {
        this.comments.add(comment);
    }

    /**
     * Add a contribution to a feature.
     * 
     * @param member the author of the contribution
     * @param team add this contribution in the name of team.
     * @param amount the > 0 amount of euros on this contribution
     * @param comment a <= 144 char comment on this contribution
     * @return the new {@link DaoContribution}
     * @throws NotEnoughMoneyException the not enough money exception
     */
    public DaoContribution addContribution(final DaoMember member, final DaoTeam team, final BigDecimal amount, final String comment)
            throws NotEnoughMoneyException {
        if (amount == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Log.data().fatal("Cannot create a contribution with this amount " + amount.toEngineeringString() + " by member " + member.getId());
            throw new BadProgrammerException("The amount of a contribution cannot be <= 0.", null);
        }
        if (comment != null && comment.length() > DaoContribution.COMMENT_MAX_LENGTH) {
            Log.data().fatal("The comment of a contribution must be <= 140 chars long.");
            throw new BadProgrammerException("Comments length of Contribution must be < 140.", null);
        }
        if (team != null && !team.getUserTeamRight(member).contains(UserTeamRight.BANK)) {
            throw new BadProgrammerException("This member cannot contribute as a team.");
        }
        final DaoContribution newContribution = new DaoContribution(member, team, this, amount, comment);
        this.contributions.add(newContribution);
        this.contribution = this.contribution.add(amount);
        return newContribution;
    }

    /**
     * Add a new offer for this feature. If there is no selected offer, select
     * this one.
     * 
     * @param offer the offer to add
     */
    public void addOffer(final DaoOffer offer) {
        this.offers.add(offer);
    }

    /**
     * delete offer from this feature AND FROM DB !.
     * 
     * @param offer the offer we want to delete.
     */
    public void removeOffer(final DaoOffer offer) {
        this.offers.remove(offer);
        if (offer.equals(this.selectedOffer)) {
            this.selectedOffer = null;
        }
        SessionManager.getSessionFactory().getCurrentSession().delete(offer);
    }

    /**
     * Compute the selected offer. WARNING this does not assign anything to the
     * selectedOffer property.
     * 
     * @return the selected offer
     */
    public DaoOffer computeSelectedOffer() {
        try {
            return (DaoOffer) SessionManager.getNamedQuery("feature.getOffers.bySelected")
                                            .setEntity("this", this)
                                            .setParameter("state", DaoKudosable.PopularityState.PENDING)
                                            .iterate()
                                            .next();
        } catch (final NoSuchElementException e) {
            return null;
        }

    }

    /**
     * Override the selected offer.
     * 
     * @param selectedOffer the offer to set selected
     */
    public void setSelectedOffer(final DaoOffer selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    /**
     * Set the validation date.
     * 
     * @param validationDate the new validation date.
     */
    public void setValidationDate(final Date validationDate) {
        this.validationDate = validationDate;
    }

    /**
     * Called by contribution when canceled.
     * 
     * @param amount the amount
     */
    void cancelContribution(final BigDecimal amount) {
        this.contribution = this.contribution.subtract(amount);
    }

    /**
     * set the feature state.
     * 
     * @param featureState the new state
     */
    public void setFeatureState(final FeatureState featureState) {
        this.featureState = featureState;
    }

    // @Override
    // public void setIsDeleted(final Boolean isDeleted) {
    // if (isDeleted) {
    // for (DaoOffer offer : offers) {
    // offer.setIsDeleted(true);
    // }
    // }
    // }

    // ======================================================================
    // Getters.
    // ======================================================================

    /** The Constant PROGRESSION_COEF. */
    private static final int PROGRESSION_COEF = 42;

    /** The Constant PROGRESSION_CONTRIBUTION_DIVISOR. */
    private static final int PROGRESSION_CONTRIBUTION_DIVISOR = 200;

    /** The Constant PROGRESSION_PERCENT. */
    public static final int PROGRESSION_PERCENT = 100;

    @Field(store = Store.YES, index = Index.UN_TOKENIZED)
    public float getProgress() {
        final DaoOffer currentOffer = getSelectedOffer();
        if (currentOffer == null) {
            return PROGRESSION_COEF * (1 - 1 / (1 + getContribution().floatValue() / PROGRESSION_CONTRIBUTION_DIVISOR));
        }
        if (currentOffer.getAmount().floatValue() != 0) {
            return (getContribution().floatValue() * PROGRESSION_PERCENT) / currentOffer.getAmount().floatValue();
        }
        return Float.POSITIVE_INFINITY;
    }

    /**
     * Gets the a description is a translatable text with an title.
     * 
     * @return the a description is a translatable text with an title
     */
    public DaoDescription getDescription() {
        return this.description;
    }

    /**
     * Gets the offers.
     * 
     * @return the offers
     */
    public PageIterable<DaoOffer> getOffers() {
        return new MappedUserContentList<DaoOffer>(this.offers);
    }

    /**
     * Gets the feature state.
     * 
     * @return the feature state
     */
    public FeatureState getFeatureState() {
        return this.featureState;
    }

    /**
     * Gets the contributions.
     * 
     * @return the contributions
     */
    public PageIterable<DaoContribution> getContributions() {
        return new MappedUserContentList<DaoContribution>(this.contributions);
    }

    public PageIterable<DaoContribution> getContributions(boolean isCanceled) {
        String qr = "feature.getContribution.";
        if (!isCanceled) {
            qr += "not";
        }
        qr += "canceled";

        return new QueryCollection<DaoContribution>(qr).setParameter("this", this).setParameter("state", DaoContribution.State.CANCELED);
    }

    /**
     * Gets the comments count.
     * 
     * @return the comments count
     */
    public Long getCommentsCount() {
        return (Long) SessionManager.getNamedQuery("feature.getComments.size").setEntity("this", this).uniqueResult();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.data.DaoCommentable#getCommentsFromQuery()
     */
    @Override
    public PageIterable<DaoComment> getComments() {
        return new MappedUserContentList<DaoComment>(comments);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.data.DaoCommentable#getLastComment()
     */
    @Override
    public DaoComment getLastComment() {
        return CommentManager.getLastComment(comments);
    }

    /**
     * The selected offer is the offer that is most likely to be validated and
     * used.
     * 
     * @return the selected offer is the offer that is most likely to be
     *         validated and used
     */
    public DaoOffer getSelectedOffer() {
        return this.selectedOffer;
    }

    /**
     * This is a calculated value with the sum of the value of all
     * contributions.
     * 
     * @return the this is a calculated value with the sum of the value of all
     *         contributions
     */
    public BigDecimal getContribution() {
        return this.contribution;
    }

    /**
     * Gets the contribution min.
     * 
     * @return the minimum value of the contributions on this feature.
     */
    public BigDecimal getContributionMin() {
        return (BigDecimal) SessionManager.getNamedQuery("feature.getAmounts.min").setEntity("this", this).uniqueResult();
    }

    /**
     * Gets the contribution max.
     * 
     * @return the maximum value of the contributions on this feature.
     */
    public BigDecimal getContributionMax() {
        return (BigDecimal) SessionManager.getNamedQuery("feature.getAmounts.max").setEntity("this", this).uniqueResult();
    }

    /**
     * Gets the contribution avg.
     * 
     * @return the average value of the contributions on this feature.
     */
    public BigDecimal getContributionAvg() {
        return (BigDecimal) SessionManager.getNamedQuery("feature.getAmounts.avg").setEntity("this", this).uniqueResult();
    }

    public BigDecimal getContributionOf(final DaoMember member) {
        final BigDecimal contributions = (BigDecimal) SessionManager.getNamedQuery("feature.getContributionOf")
                                                                    .setEntity("this", this)
                                                                    .setEntity("member", member)
                                                                    .uniqueResult();
        return contributions != null ? contributions : BigDecimal.ZERO;
    }

    /**
     * Gets the validation date.
     * 
     * @return the validation date
     */
    public Date getValidationDate() {
        return this.validationDate;
    }

    /**
     * Gets the software.
     * 
     * @return the software
     */
    public DaoSoftware getSoftware() {
        return this.software;
    }

    /**
     * Count open bugs.
     * 
     * @return the int
     */
    public int countOpenBugs() {
        final Query query = SessionManager.getNamedQuery("feature.getBugs.byNonState.size");
        query.setEntity("offer", this.selectedOffer);
        query.setParameter("state", DaoBug.BugState.RESOLVED);
        return ((Long) query.uniqueResult()).intValue();
    }

    /**
     * Gets the open bugs.
     * 
     * @return the open bugs
     */
    public PageIterable<DaoBug> getOpenBugs() {
        if (this.selectedOffer == null) {
            return new EmptyPageIterable<DaoBug>();
        }
        return new QueryCollection<DaoBug>("feature.getBugs.byNonState").setEntity("offer", this.selectedOffer)
                                                                        .setParameter("state", DaoBug.BugState.RESOLVED);
    }

    /**
     * Gets the closed bugs.
     * 
     * @return the closed bugs
     */
    public PageIterable<DaoBug> getClosedBugs() {
        return new QueryCollection<DaoBug>("feature.getBugs.byState").setEntity("offer", this.selectedOffer).setParameter("state",
                                                                                                                          DaoBug.BugState.RESOLVED);
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
    protected DaoFeature() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoFeature other = (DaoFeature) obj;
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
