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
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * An offer is a developer offer to a demand.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "offer.getBatches",
                           query = "FROM DaoBatch WHERE offer = :this ORDER BY expirationDate, id"),
                        @NamedQuery(
                           name = "offer.getBatches.size",
                           query = "SELECT count(*) FROM DaoBatch WHERE offer = :this"),
                       }
             )
// @formatter:on
public class DaoOffer extends DaoKudosable {

    /**
     * This is demand on which this offer is done.
     */
    @ManyToOne(optional = false)
    private DaoFeature demand;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    @OrderBy("expirationDate ASC")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoBatch> batches = new ArrayList<DaoBatch>();

    /**
     * The expirationDate is calculated from the batches variables.
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date expirationDate;

    @Basic(optional = false)
    private int currentBatch;

    /**
     * The amount represents the money the member want to have to make his
     * offer. This is a calculated field used for performance speedup.
     * <code>(= foreach batches; amount += baches.getAmount)</code>
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
     * @param demand is the demand on which this offer is made. Must be non
     *            null.
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws FatalErrorException if the amount is < 0 or if the Date is in the
     *             future.
     */
    public DaoOffer(final DaoMember member,
                    final DaoFeature demand,
                    final BigDecimal amount,
                    final DaoDescription description,
                    final Date dateExpire,
                    final int secondsBeforeValidation) {
        super(member);
        if (demand == null) {
            throw new NonOptionalParameterException();
        }
        this.demand = demand;
        this.amount = BigDecimal.ZERO; // Will be updated by addBatch
        this.expirationDate = new Date();// Will be updated by addBatch
        this.currentBatch = 0;
        this.setDraft(true);
        addBatch(new DaoBatch(dateExpire, amount, description, this, secondsBeforeValidation));
    }

    public void cancelEverythingLeft() {
        for (int i = this.currentBatch; i < this.batches.size(); ++i) {
            this.batches.get(i).cancelBatch();
        }
        this.currentBatch = this.batches.size();
    }

    public void addBatch(final DaoBatch batch) {
        if (isDraft() == false) {
            throw new FatalErrorException("You cannot add a batch on a non draft offer.");
        }
        this.amount = batch.getAmount().add(this.amount);
        final Date expiration = batch.getExpirationDate();
        if (this.expirationDate.before(expiration)) {
            this.expirationDate = expiration;
        }
        this.batches.add(batch);
    }

    public boolean hasBatchesLeft() {
        return this.currentBatch < this.batches.size();
    }

    void passToNextBatch() {
        this.currentBatch++;
    }

    void batchHasARelease(final DaoBatch batch) {
        // Find next batch. Passe it into developing state.
        for (int i = 0; i < this.batches.size(); ++i) {
            if (this.batches.get(i).equals(batch)) {
                if ((i + 1) < this.batches.size()) {
                    this.batches.get(i + 1).setDeveloping();
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
     * @return All the batches for this offer. (Even the MasterBatch).
     */
    public PageIterable<DaoBatch> getBatches() {
        return new QueryCollection<DaoBatch>("offer.getBatches").setEntity("this", this);
    }

    public DaoBatch getCurrentBatch() {
        return this.batches.get(this.currentBatch);
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

    // TODO comment; it make sure the sum returned is 100.
    int getBatchPercent(final DaoBatch current) {
        if (this.batches.size() == 1) {
            return 100;
        }

        int alreadyReturned = 0;
        for (int i = 0; i < this.batches.size(); ++i) {
            // Calculate the percent of the batch
            final DaoBatch batch = this.batches.get(i);
            final int percent = batch.getAmount().divide(this.amount, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100")).intValue();
            if (current.equals(batch)) {
                // is the current is the last one
                if (i == (this.batches.size() - 1)) {
                    return 100 - alreadyReturned;
                }
                return percent;
            }
            // Save how much has been sent.
            alreadyReturned += percent;
        }
        throw new FatalErrorException("This offer has no batch, or the 'current' batch isn't found");
    }

    public boolean hasRelease() {
        final Query query = SessionManager.createFilter(this.batches, "SELECT count(*) WHERE this.releases is not empty");
        return !((Long) query.uniqueResult()).equals(0L);
    }

    public DaoRelease getLastRelease() {
        String q = "FROM DaoRelease WHERE creationDate = (SELECT max(r.creationDate) " + //
                "FROM DaoOffer as o " + //
                "INNER JOIN o.batches as b " + //
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

    public DaoFeature getDemand() {
        return this.demand;
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
        result = prime * result + ((this.demand == null) ? 0 : this.demand.hashCode());
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
        if (this.demand == null) {
            if (other.demand != null) {
                return false;
            }
        } else if (!this.demand.equals(other.demand)) {
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
