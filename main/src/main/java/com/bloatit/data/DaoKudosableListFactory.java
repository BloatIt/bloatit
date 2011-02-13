package com.bloatit.data;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoKudosable.PopularityState;

class DaoKudosableListFactory<T extends DaoKudosable> extends DaoUserContentListFactory<T> {

    protected DaoKudosableListFactory(Criteria criteria) {
        super(criteria);
    }

    public DaoKudosableListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoKudosable.class));
    }

    public void orderByPopularity(DaoAbstractListFactory.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc("popularity"));
        } else {
            addOrder(Order.desc("popularity"));
        }
    }

    public void popularity(Comparator cmp, int value) {
        add(createNbCriterion(cmp, "popularity", value));
    }

    public void stateEquals(PopularityState state) {
        add(Restrictions.eq("state", state));
    }
    
    public void kudosSize(Comparator cmp, int number){
        add(createNbCriterion(cmp, "kudos", number));
    }

}
