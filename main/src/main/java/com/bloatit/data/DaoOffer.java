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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.HibernateException;
import org.hibernate.Session;
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
public final class DaoOffer extends DaoKudosable {

    /**
     * This is demand on which this offer is done.
     */
    @ManyToOne(optional = false)
    private DaoDemand demand;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    @OrderBy("expirationDate ASC")
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

    // ======================================================================
    // Construction
    // ======================================================================

    public static DaoOffer createAndPersist(final DaoMember member,
                                            final DaoDemand demand,
                                            final BigDecimal amount,
                                            final DaoDescription description,
                                            final Date dateExpire,
                                            final int secondsBeforeValidation) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoOffer offer = new DaoOffer(member, demand);
        try {
            session.save(offer);
            // Must be done here because of non null property in addBatch.
            offer.addBatch(DaoBatch.createAndPersist(dateExpire, amount, description, offer, secondsBeforeValidation));
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return offer;
    }

    public static DaoOffer createAndPersist(final DaoMember member, final DaoDemand demand) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoOffer offer = new DaoOffer(member, demand);
        try {
            session.save(offer);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return offer;
    }

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
    private DaoOffer(final DaoMember member, final DaoDemand demand) {
        super(member);
        if (demand == null) {
            throw new NonOptionalParameterException();
        }
        this.demand = demand;
        this.amount = BigDecimal.ZERO; // Will be updated by addBatch
        this.expirationDate = new Date();// Will be updated by addBatch
        this.currentBatch = 0;
    }

    public void cancelEverythingLeft() {
        for (int i = currentBatch; i < batches.size(); ++i) {
            batches.get(i).cancelBatch();
        }
        currentBatch = batches.size();
    }

    public void addBatch(final DaoBatch batch) {
        amount = batch.getAmount().add(amount);
        final Date expiration = batch.getExpirationDate();
        if (expirationDate.before(expiration)) {
            expirationDate = expiration;
        }
        batches.add(batch);
    }

    public boolean hasBatchesLeft() {
        return currentBatch < batches.size();
    }

    void passToNextBatch() {
        currentBatch++;
    }

    void batchHasARelease(DaoBatch batch) {
        // Find next batch. Passe it into developing state.
        for (int i = 0; i < batches.size(); ++i) {
            if (batches.get(i).equals(batch)) {
                if (batches.size() < (i + 1)) {
                    batches.get(i + 1).setDeveloping();
                }
                break;
            }
        }
    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * @return All the batches for this offer. (Even the MasterBatch).
     */
    public PageIterable<DaoBatch> getBatches() {
        final String query = "from DaoBatch where offer = :this order by expirationDate";
        final String queryCount = "select count(*) from DaoBatch where offer = :this";
        return new QueryCollection<DaoBatch>( //
                                             SessionManager.createQuery(query).setEntity("this", this),//
                                             SessionManager.createQuery(queryCount).setEntity("this", this));//
    }

    public DaoBatch getCurrentBatch() {
        return batches.get(currentBatch);
    }

    /**
     * @return a cloned version of the expirationDate attribute.
     */
    public Date getExpirationDate() {
        return (Date) expirationDate.clone();
    }

    public BigDecimal getAmount() {
        return amount;
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

    public DaoDemand getDemand() {
        return demand;
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
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((demand == null) ? 0 : demand.hashCode());
        result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
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
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (demand == null) {
            if (other.demand != null) {
                return false;
            }
        } else if (!demand.equals(other.demand)) {
            return false;
        }
        if (expirationDate == null) {
            if (other.expirationDate != null) {
                return false;
            }
        } else if (!expirationDate.equals(other.expirationDate)) {
            return false;
        }
        return true;
    }

}
