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

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.SessionManager;

/**
 * The Class DaoKudosableQuery is a generic way of querying for DaoKudosables using criterias.
 * 
 * @param <T> the generic type
 */
public class DaoKudosableQuery<T extends DaoKudosable> extends DaoUserContentQuery<T> {

    private static String IS_POPULARITY_LOCKED = "isPopularityLocked";
    private static String KUDOS = "kudos";
    private static String STATE = "state";
    private static String POPULARITY = "popularity";

    /**
     * Instantiates a new dao kudosable query.
     * 
     * @param criteria the criteria used to query kudosables
     */
    protected DaoKudosableQuery(final Criteria criteria) {
        super(criteria);
    }

    /**
     * Instantiates a new dao kudosable query.
     */
    public DaoKudosableQuery() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoKudosable.class));
    }

    /**
     * Order by popularity.
     * 
     * @param order the order
     */
    public void orderByPopularity(final DaoAbstractQuery.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(POPULARITY));
        } else {
            addOrder(Order.desc(POPULARITY));
        }
    }

    /**
     * Add a restriction on the popularity
     * 
     * @param cmp the comparator
     * @param value the value to compare to.
     */
    public void popularity(final Comparator cmp, final int value) {
        add(createNbCriterion(cmp, POPULARITY, value));
    }

    /**
     * Add a restriction on the state
     * 
     * @param state the state
     */
    public void stateEquals(final PopularityState state) {
        add(Restrictions.eq(STATE, state));
    }

    /**
     * Add a restriction on the number of kudos
     * 
     * @param cmp the comparator
     * @param number the number to compare to the number of kudos.
     */
    public void kudosSize(final Comparator cmp, final int number) {
        add(createNbCriterion(cmp, KUDOS, number));
    }

    /**
     * Restrict this query to the locked kudosable.
     */
    public void lokedOnly() {
        add(Restrictions.eq(IS_POPULARITY_LOCKED, true));
    }

    /**
     * Restrict this query to the unlocked kudosable.
     */
    public void nonLokedOnly() {
        add(Restrictions.eq(IS_POPULARITY_LOCKED, false));
    }

}
