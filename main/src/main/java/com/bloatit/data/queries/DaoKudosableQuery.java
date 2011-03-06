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

public class DaoKudosableQuery<T extends DaoKudosable> extends DaoUserContentQuery<T> {

    private static  String IS_POPULARITY_LOCKED = "isPopularityLocked";
    private static  String KUDOS = "kudos";
    private static  String STATE = "state";
    private static  String POPULARITY = "popularity";

    protected DaoKudosableQuery( Criteria criteria) {
        super(criteria);
    }

    public DaoKudosableQuery() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoKudosable.class));
    }

    public void orderByPopularity( DaoAbstractQuery.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(POPULARITY));
        } else {
            addOrder(Order.desc(POPULARITY));
        }
    }

    public void popularity( Comparator cmp,  int value) {
        add(createNbCriterion(cmp, POPULARITY, value));
    }

    public void stateEquals( PopularityState state) {
        add(Restrictions.eq(STATE, state));
    }

    public void kudosSize( Comparator cmp,  int number) {
        add(createNbCriterion(cmp, KUDOS, number));
    }

    public void lokedOnly() {
        add(Restrictions.eq(IS_POPULARITY_LOCKED, true));
    }

    public void nonLokedOnly() {
        add(Restrictions.eq(IS_POPULARITY_LOCKED, false));
    }

}
