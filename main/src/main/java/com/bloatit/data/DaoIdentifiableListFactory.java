package com.bloatit.data;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

class DaoIdentifiableListFactory<T extends DaoIdentifiable> extends DaoAbstractListFactory<T> {

    protected DaoIdentifiableListFactory(Criteria criteria) {
        super(criteria);
    }

    public DaoIdentifiableListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoIdentifiable.class));
    }

    public void withId(Integer id) {
        add(Restrictions.eq("id", id));
    }
}
