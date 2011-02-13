package com.bloatit.data;

import org.hibernate.Criteria;

class DaoIdentifiableListFactory<T extends DaoIdentifiable> extends DaoAbstractListFactory<T> {

    protected DaoIdentifiableListFactory(Criteria criteria) {
        super(criteria);
    }
}