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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.Query;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * An offer is a developer offer to a feature.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "offer.getMilestones",
                           query = "FROM DaoMilestone WHERE offer = :this ORDER BY expirationDate, id"),
                        @NamedQuery(
                           name = "offer.getMilestones.size",
                           query = "SELECT count(*) FROM DaoMilestone WHERE offer = :this"),
                       }
             )
// @formatter:on
public class DaoOffer extends DaoKudosable {

    /**
     * This is feature on which this offer is done.
     */
    @ManyToOne(optional = false)
    private DaoFeature feature;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    @OrderBy("expirationDate ASC")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoMilestone> milestones = new ArrayList<DaoMilestone>();

    /**
     * The expirationDate is calculated from the milestones variables.
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date expirationDate;

    @Basic(optional = false)
    private int currentMilestone;

    /**
     * The amount represents the money the member want to have to make his
     * offer. This is a calculated field used for performance speedup.
     * <code>(= foreach milestones; amount += baches.getAmount)</code>
     */
    @Basic(optional = false)
    private BigDecimal amount;

    @Basic(optional = false)
    private boolean isDraft;

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Create a DaoOffer.
     * 
     * @param member is the author of the offer. Must be non null.
     * @param feature is the feature on which this offer is made. Must be non
     *            null.
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws BadProgrammerException if the amount is < 0 or if the Date is in
     *             the future.
     */
    public DaoOffer(final DaoMember member,
                    final DaoTeam team,
                    final DaoFeature feature,
                    final BigDecimal amount,
                    final DaoDescription description,
                    final Date dateExpire,
                    final int secondsBeforeValidation) {
        super(member, team);
        if (feature == null) {
            throw new NonOptionalParameterException();
        }
        this.feature = feature;
        this.amount = BigDecimal.ZERO; // Will be updated by addMilestone
        this.expirationDate = new Date();// Will be updated by addMilestone
        this.currentMilestone = 0;
        this.setDraft(true);
        addMilestone(new DaoMilestone(dateExpire, amount, description, this, secondsBeforeValidation));
    }

    public void cancelEverythingLeft() {
        for (int i = this.currentMilestone; i < this.milestones.size(); ++i) {
            this.milestones.get(i).cancelMilestone();
        }
        this.currentMilestone = this.milestones.size();
    }

    public void addMilestone(final DaoMilestone milestone) {
        if (isDraft() == false) {
            throw new BadProgrammerException("You cannot add a milestone on a non draft offer.");
        }
        this.amount = milestone.getAmount().add(this.amount);
        final Date expiration = milestone.getExpirationDate();
        if (this.expirationDate.before(expiration)) {
            this.expirationDate = expiration;
        }
        this.milestones.add(milestone);
    }

    public boolean hasMilestoneesLeft() {
        return this.currentMilestone < this.milestones.size();
    }

    void passToNextMilestone() {
        this.currentMilestone++;
    }

    void milestoneHasARelease(final DaoMilestone milestone) {
        // Find next milestone. Passe it into developing state.
        for (int i = 0; i < this.milestones.size(); ++i) {
            if (this.milestones.get(i).equals(milestone)) {
                if ((i + 1) < this.milestones.size()) {
                    this.milestones.get(i + 1).setDeveloping();
                }
                break;
            }
        }
    }

    public void setDraft(final boolean isDraft) {
        this.isDraft = isDraft;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public boolean isDraft() {
        return this.isDraft;
    }

    /**
     * @return All the milestones for this offer. (Even the MasterMilestone).
     */
    public PageIterable<DaoMilestone> getMilestonees() {
        return new QueryCollection<DaoMilestone>("offer.getMilestonees").setEntity("this", this);
    }

    public DaoMilestone getCurrentMilestone() {
        return this.milestones.get(this.currentMilestone);
    }

    /**
     * @return a cloned version of the expirationDate attribute.
     */
    public Date getExpirationDate() {
        return (Date) this.expirationDate.clone();
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Get the percent of this offer amount to give to the developer for this
     * milestone. This method make sure that the sum of all the milestone
     * percent is 100.
     * 
     * @param current the milestone on which we want to know the percent.
     * @return an int between [0;100]
     */
    int getMilestonePercent(final DaoMilestone current) {
        if (this.milestones.size() == 1) {
            return 100;
        }

        int alreadyReturned = 0;
        for (int i = 0; i < this.milestones.size(); ++i) {
            // Calculate the percent of the milestone
            final DaoMilestone milestone = this.milestones.get(i);
            final int percent = milestone.getAmount().divide(this.amount, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100")).intValue();
            if (current.equals(milestone)) {
                // if the current is the last one
                if (i == (this.milestones.size() - 1)) {
                    return 100 - alreadyReturned;
                }
                return percent;
            }
            // Save how much has been sent.
            alreadyReturned += percent;
        }
        throw new BadProgrammerException("This offer has no milestone, or the 'current' milestone isn't found");
    }

    public boolean hasRelease() {
        final Query query = SessionManager.createFilter(this.milestones, "SELECT count(*) WHERE this.releases is not empty");
        return !((Long) query.uniqueResult()).equals(0L);
    }

    public DaoRelease getLastRelease() {
        final String q = "FROM DaoRelease WHERE creationDate = (SELECT max(r.creationDate) " + //
                "FROM DaoOffer as o " + //
                "INNER JOIN o.milestones as b " + //
                "INNER JOIN b.releases as r " + //
                "WHERE o=:this)";

        final Query query = SessionManager.createQuery(q).setEntity("this", this);

        return (DaoRelease) query.uniqueResult();
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

    protected DaoOffer() {
        super();
    }

    public DaoFeature getFeature() {
        return this.feature;
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
        int result = super.hashCode();
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.feature == null) ? 0 : this.feature.hashCode());
        result = prime * result + ((this.expirationDate == null) ? 0 : this.expirationDate.hashCode());
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
        final DaoOffer other = (DaoOffer) obj;
        if (this.amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!this.amount.equals(other.amount)) {
            return false;
        }
        if (this.feature == null) {
            if (other.feature != null) {
                return false;
            }
        } else if (!this.feature.equals(other.feature)) {
            return false;
        }
        if (this.expirationDate == null) {
            if (other.expirationDate != null) {
                return false;
            }
        } else if (!this.expirationDate.equals(other.expirationDate)) {
            return false;
        }
        return true;
    }

}
