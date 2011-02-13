package com.bloatit.data;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.bloatit.framework.utils.PageIterable;

abstract class DaoAbstractListFactory<T extends DaoIdentifiable> {
    private final Criteria criteria;
    private final ProjectionList projections = Projections.projectionList();

    public enum OrderType {
        ASC, DESC
    }

    public DaoAbstractListFactory(Criteria criteria) {
        super();
        this.criteria = criteria;
    }

    public final PageIterable<T> createCollection() {
        criteria.setProjection(projections);
        return new CriteriaCollection<T>(criteria);
    }

    public Criteria add(Criterion criterion) {
        return criteria.add(criterion);
    }

    public Criteria addOrder(Order order) {
        return criteria.addOrder(order);
    }

    public ProjectionList add(Projection proj) {
        return projections.add(proj);
    }

    public ProjectionList add(Projection projection, String alias) {
        return projections.add(projection, alias);
    }
}