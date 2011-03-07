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
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.bloatit.common.Log;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.data.search.DaoDemandSearchFilterFactory;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A DaoDemand is a kudosable content. It has a translatable description, and
 * can have a specification and some offers. The state of the demand is managed
 * by its super class DaoKudosable. On a demand we can add some comment and some
 * contriutions.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed
@FullTextFilterDef(name = "searchFilter", impl = DaoDemandSearchFilterFactory.class)
//@formatter:off
@NamedQueries(value = {@NamedQuery(
                           name = "demand.getOffers.bySelected",
                           query = "FROM DaoOffer " + 
                                   "WHERE demand = :this " + 
                                   "AND state <= :state " + // <= PENDING and VALIDATED.
                                   "AND popularity = (select max(popularity) from DaoOffer where demand = :this) " + //
                                   "AND popularity >= 0 " + 
                                   "ORDER BY amount ASC, creationDate DESC"),
                                   
                        @NamedQuery(
                           name = "demand.getAmounts.min",
                           query = "SELECT min(amount) FROM DaoContribution WHERE demand = :this"),
                           
                        @NamedQuery(
                           name = "demand.getAmounts.max",
                           query = "SELECT max(amount) FROM DaoContribution WHERE demand = :this"),
                           
                       @NamedQuery(
                           name = "demand.getAmounts.avg",
                           query = "SELECT avg(amount) FROM DaoContribution WHERE demand = :this"),
                           
                        @NamedQuery(
                            name = "demand.getBugs.byNonState",
                            query = "SELECT bugs_ " + 
                                    "FROM com.bloatit.data.DaoOffer offer_ " + 
                                    "JOIN offer_.batches as bs " + 
                                    "JOIN bs.bugs as bugs_ " + 
                                    "WHERE offer_ = :offer " + 
                                    "AND bugs_.state != :state "),
                                    
                        @NamedQuery(
                            name = "demand.getBugs.byNonState.size",
                            query = "SELECT count(bugs_) " + 
                                    "FROM com.bloatit.data.DaoOffer offer_ " + 
                                    "JOIN offer_.batches as bs " + 
                                    "JOIN bs.bugs as bugs_ " + 
                                    "WHERE offer_ = :offer " + 
                                    "AND bugs_.state != :state "),
                                    
                        @NamedQuery(
                            name = "demand.getBugs.byState",
                            query = "SELECT bugs_ " + 
                                    "FROM com.bloatit.data.DaoOffer offer_ " + 
                                    "JOIN offer_.batches as bs " + 
                                    "JOIN bs.bugs as bugs_ " + 
                                    "WHERE offer_ = :offer " + 
                                    "AND bugs_.state = :state "),
                                    
                        @NamedQuery(
                            name = "demand.getBugs.byState.size",
                            query = "SELECT count(bugs_) " + 
                                    "FROM com.bloatit.data.DaoOffer offer_ " + 
                                    "JOIN offer_.batches as bs " + 
                                    "JOIN bs.bugs as bugs_ " + 
                                    "WHERE offer_ = :offer " + 
                                    "AND bugs_.state = :state "),
                     }
             )
// @formatter:on
public class DaoDemand extends DaoKudosable implements DaoCommentable {

    /**
     * This is the state of the demand. It's used in the workflow modeling. The
     * order is important !
     */
    public enum DemandState {
        /** No offers, waiting for money and offer */
        PENDING,

        /** One or more offer, waiting for money */
        PREPARING,

        /** Development in progress */
        DEVELOPPING,

        /** Something went wrong, the demand is canceled */
        DISCARDED,

        /** All is good, the developer is paid and the users are happy */
        FINISHED
    }

    /**
     * This is a calculated value with the sum of the value of all
     * contributions.
     */
    @Basic(optional = false)
    @Field(store = Store.NO)
    private BigDecimal contribution;

    @Basic(optional = false)
    @Field(store = Store.NO)
    @Enumerated
    private DemandState demandState;

    /**
     * A description is a translatable text with an title.
     */
    @OneToOne(optional = false)
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @IndexedEmbedded
    private DaoDescription description;

    @OneToMany(mappedBy = "demand")
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @IndexedEmbedded
    private List<DaoOffer> offers = new ArrayList<DaoOffer>(0);

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<DaoContribution> contributions = new ArrayList<DaoContribution>(0);

    @OneToMany(mappedBy = "demand")
    @OrderBy(clause = "id")
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @IndexedEmbedded
    public List<DaoComment> comments = new ArrayList<DaoComment>();

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

    @ManyToOne(fetch=FetchType.LAZY)
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @IndexedEmbedded
    private DaoProject project;

    @Basic(optional = true)
    private Date validationDate;

    // ======================================================================
    // Construct.
    // ======================================================================

    /**
     * @see #DaoDemand(DaoMember, DaoDescription, DaoProject)
     */
    public static DaoDemand createAndPersist(final DaoMember member, final DaoDescription description, final DaoProject project) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoDemand demand = new DaoDemand(member, description, project);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return demand;
    }

    /**
     * Create a DaoDemand and set its state to the state PENDING.
     * 
     * @param member is the author of the demand
     * @param description is the description ...
     * @throws NonOptionalParameterException if any of the parameter is null.
     */
    private DaoDemand(final DaoMember member, final DaoDescription description, final DaoProject project) {
        super(member);
        if (description == null || project == null) {
            throw new NonOptionalParameterException();
        }
        this.project = project;
        project.addDemand(this);
        this.description = description;
        this.validationDate = null;
        setSelectedOffer(null);
        this.contribution = BigDecimal.ZERO;
        setDemandState(DemandState.PENDING);
    }

    /**
     * Delete this DaoDemand from the database. "this" will remain, but
     * unmapped. (You shoudn't use it then)
     */
    public void delete() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        session.delete(this);
    }

    @Override
    public void addComment(final DaoComment comment) {
        this.comments.add(comment);
    }

    /**
     * Add a contribution to a demand.
     * 
     * @param member the author of the contribution
     * @param amount the > 0 amount of euros on this contribution
     * @param comment a <= 144 char comment on this contribution
     * @throws NotEnoughMoneyException
     */
    public void addContribution(final DaoMember member, final BigDecimal amount, final String comment) throws NotEnoughMoneyException {
        if (amount == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Log.data().fatal("Cannot create a contribution with this amount " + amount.toEngineeringString() + " by member " + member.getId());
            throw new FatalErrorException("The amount of a contribution cannot be <= 0.", null);
        }
        if (comment != null && comment.length() > DaoContribution.COMMENT_MAX_LENGTH) {
            Log.data().fatal("The comment of a contribution must be <= 144 chars long.");
            throw new FatalErrorException("Comments lenght of Contribution must be < 144.", null);
        }

        this.contributions.add(new DaoContribution(member, this, amount, comment));
        this.contribution = this.contribution.add(amount);
    }

    /**
     * Add a new offer for this demand. If there is no selected offer, select
     * this one.
     */
    public void addOffer(final DaoOffer offer) {
        this.offers.add(offer);
    }

    /**
     * delete offer from this demand AND FROM DB !
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

    public void computeSelectedOffer() {
        this.selectedOffer = getCurrentOffer();
    }

    public void setSelectedOffer(final DaoOffer selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    public void setValidationDate(final Date validationDate) {
        this.validationDate = validationDate;
    }

    void validateContributions(final int percent) {
        if (this.selectedOffer == null) {
            throw new FatalErrorException("The selectedOffer shouldn't be null here !");
        }
        if (percent == 0) {
            return;
        }
        for (final DaoContribution contribution : getContributions()) {
            try {
                if (contribution.getState() == DaoContribution.State.PENDING) {
                    contribution.validate(this.selectedOffer, percent);
                }
            } catch (final NotEnoughMoneyException e) {
                Log.data().fatal("Cannot validate contribution, not enought money.", e);
            }
        }
    }

    /**
     * Called by contribution when canceled.
     * 
     * @param amount
     */
    void cancelContribution(final BigDecimal amount) {
        this.contribution = this.contribution.subtract(amount);
    }

    public void setDemandState(final DemandState demandState) {
        this.demandState = demandState;
    }

    // ======================================================================
    // Getters.
    // ======================================================================

    public DaoDescription getDescription() {
        return this.description;
    }

    /**
     * The current offer is the offer with the max popularity then the min
     * amount.
     * 
     * @return the current offer for this demand, or null if there is no offer.
     */
    private DaoOffer getCurrentOffer() {
        try {
            return (DaoOffer) SessionManager.getNamedQuery("demand.getOffers.bySelected")
                                            .setEntity("this", this)
                                            .setParameter("state", DaoKudosable.PopularityState.PENDING)
                                            .iterate()
                                            .next();
        } catch (final NoSuchElementException e) {
            return null;
        }
    }

    public PageIterable<DaoOffer> getOffers() {
        return new MappedList<DaoOffer>(this.offers);
    }

    public DemandState getDemandState() {
        return this.demandState;
    }

    public PageIterable<DaoContribution> getContributions() {
        return new MappedList<DaoContribution>(this.contributions);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.data.DaoCommentable#getCommentsFromQuery()
     */
    @Override
    public PageIterable<DaoComment> getComments() {
        return new MappedList<DaoComment>(this.comments);
    }

    @Override
    public DaoComment getLastComment() {
        return CommentManager.getLastComment(comments);
    }

    public DaoOffer getSelectedOffer() {
        return this.selectedOffer;
    }

    public BigDecimal getContribution() {
        return this.contribution;
    }

    /**
     * @return the minimum value of the contributions on this demand.
     */
    public BigDecimal getContributionMin() {
        return (BigDecimal) SessionManager.getNamedQuery("demand.getAmounts.min").setEntity("this", this).uniqueResult();
    }

    /**
     * @return the maximum value of the contributions on this demand.
     */
    public BigDecimal getContributionMax() {
        return (BigDecimal) SessionManager.getNamedQuery("demand.getAmounts.max").setEntity("this", this).uniqueResult();
    }

    /**
     * @return the average value of the contributions on this demand.
     */
    public BigDecimal getContributionAvg() {
        return (BigDecimal) SessionManager.getNamedQuery("demand.getAmounts.avg").setEntity("this", this).uniqueResult();
    }

    public Date getValidationDate() {
        return this.validationDate;
    }

    /**
     * @return the project
     */
    public DaoProject getProject() {
        return this.project;
    }

    public int countOpenBugs() {
        final Query query = SessionManager.getNamedQuery("demand.getBugs.byNonState.size");
        query.setEntity("offer", this.selectedOffer);
        query.setParameter("state", DaoBug.BugState.RESOLVED);
        return ((Long) query.uniqueResult()).intValue();
    }

    public PageIterable<DaoBug> getOpenBugs() {
        return new QueryCollection<DaoBug>("demand.getBugs.byNonState").setEntity("offer", this.selectedOffer).setParameter("state",
                                                                                                                            DaoBug.BugState.RESOLVED);
    }

    public PageIterable<DaoBug> getClosedBugs() {
        return new QueryCollection<DaoBug>("demand.getBugs.byState").setEntity("offer", this.selectedOffer).setParameter("state",
                                                                                                                         DaoBug.BugState.RESOLVED);
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

    protected DaoDemand() {
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
        final DaoDemand other = (DaoDemand) obj;
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
