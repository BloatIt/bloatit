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
package com.bloatit.data.queries;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.data.SessionManager;

/**
 * A query on DaoMilestone, using {@link Criteria}.
 */
public class DaoMilestoneQuery extends DaoIdentifiableQuery<DaoMilestone> {

    /**
     * Instantiates a new dao milestone list factory.
     */
    public DaoMilestoneQuery() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoMilestone.class));
    }

    /**
     * Add a WHERE close restricting the <code>amount</code> value of the
     * returning milestonees. For example if you want your query to return only the
     * milestonees that have less than 42 €, you can call:
     *
     * <pre>
     * DaoOfferListFactory factory = new DaoMilestoneesListFactory();
     * factory.amount(Comparator.LESS, 42);
     * PageIterable&lt;DaoMilestonees&gt; milestonees = factory.createCollection();
     * </pre>
     *
     * @param cmp the cmp.
     * @param value the value
     */
    public void amount(final Comparator cmp, final BigDecimal value) {
        add(createNbCriterion(cmp, "amount", value));
    }

    /**
     * Add a close on the state of this milestone.
     *
     * @param state the state of the resulting milestonees.
     */
    public void stateEquals(final MilestoneState state) {
        add(Restrictions.eq("milestoneState", state));
    }

    /**
     * Make sure this query will only return milestone having released.
     */
    public void withRelease() {
        add(Restrictions.isNotEmpty("releases"));
    }

    /**
     * Make sure this query will only return milestone having no released.
     */
    public void withoutRelease() {
        add(Restrictions.isEmpty("releases"));
    }

}
