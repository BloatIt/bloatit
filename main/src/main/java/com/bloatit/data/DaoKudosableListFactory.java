package com.bloatit.data;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoKudosable.PopularityState;

class DaoKudosableListFactory<T extends DaoKudosable> extends DaoUserContentListFactory<T> {

    private static final String KUDOS = "kudos";
    private static final String STATE = "state";
    private static final String POPULARITY = "popularity";

    protected DaoKudosableListFactory(Criteria criteria) {
        super(criteria);
    }

    public DaoKudosableListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoKudosable.class));
    }

    public void orderByPopularity(DaoAbstractListFactory.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(POPULARITY));
        } else {
            addOrder(Order.desc(POPULARITY));
        }
    }

    public void popularity(Comparator cmp, int value) {
        add(createNbCriterion(cmp, POPULARITY, value));
    }

    public void stateEquals(PopularityState state) {
        add(Restrictions.eq(STATE, state));
    }

    public void kudosSize(Comparator cmp, int number) {
        add(createNbCriterion(cmp, KUDOS, number));
    }

}
