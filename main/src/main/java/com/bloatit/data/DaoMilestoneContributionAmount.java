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

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Represent the amount paid for a contribution, for a spécific milestone.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries(value = {@NamedQuery(
                           name = "milestone.contribution.exist",
                           query = "FROM DaoMilestoneContributionAmount " +
                           		"WHERE milestone = :milestone " +
                           		"AND contribution = :contribution"),
                     }
             )
public class DaoMilestoneContributionAmount extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMilestone milestone;

    @ManyToOne(optional = false)
    private DaoContribution contribution;

    @Basic(optional = false)
    private BigDecimal amount;

    protected DaoMilestoneContributionAmount(final DaoMilestone milestone, final DaoContribution contribution, final BigDecimal amount) {
        super();

        checkOptionnal(milestone, contribution, amount);

        this.milestone = milestone;
        this.contribution = contribution;
        this.amount = amount;
    }

    /**
     * Creates the DaoMilestoneContributionAmount and persist it.
     */
    private static DaoMilestoneContributionAmount createAndPersist(final DaoMilestone milestone,
                                                                  final DaoContribution contribution,
                                                                  final BigDecimal amount) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoMilestoneContributionAmount milestoneContribtuAmount = new DaoMilestoneContributionAmount(milestone, contribution, amount);
        try {
            session.save(milestoneContribtuAmount);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return milestoneContribtuAmount;
    }

    public static DaoMilestoneContributionAmount updateOrCreate(final DaoMilestone milestone,
                                                                  final DaoContribution contribution,
                                                                  final BigDecimal amount) {
        DaoMilestoneContributionAmount milestoneContribtuAmount =  (DaoMilestoneContributionAmount) SessionManager.getNamedQuery("milestone.contribution.exist").setEntity("milestone", milestone).setEntity("contribution", contribution).uniqueResult();

        if(milestoneContribtuAmount == null) {
            milestoneContribtuAmount = createAndPersist(milestone, contribution, amount);
        } else {
            milestoneContribtuAmount.addAmount(amount);
        }

        return milestoneContribtuAmount;
    }


    private void addAmount(BigDecimal newAmount) {
        amount = amount.add(newAmount);
    }

    public DaoMilestone getMilestone() {
        return milestone;
    }

    public DaoContribution getContribution() {
        return contribution;
    }

    public BigDecimal getAmount() {
        return amount;
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
    protected DaoMilestoneContributionAmount() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((contribution == null) ? 0 : contribution.hashCode());
        result = prime * result + ((milestone == null) ? 0 : milestone.hashCode());
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
        DaoMilestoneContributionAmount other = (DaoMilestoneContributionAmount) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (contribution == null) {
            if (other.contribution != null)
                return false;
        } else if (!contribution.equals(other.contribution))
            return false;
        if (milestone == null) {
            if (other.milestone != null)
                return false;
        } else if (!milestone.equals(other.milestone))
            return false;
        return true;
    }

}
