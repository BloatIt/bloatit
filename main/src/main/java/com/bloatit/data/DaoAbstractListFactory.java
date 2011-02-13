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
        prepareCriteria();
        return new CriteriaCollection<T>(criteria);
    }

    @SuppressWarnings("unchecked")
    public final T uniqueResult() {
        prepareCriteria();
        return (T) criteria.uniqueResult();
    }

    private void prepareCriteria() {
        if (projections.getLength() > 0) {
            criteria.setProjection(projections);
        }
    }

    protected Criteria add(Criterion criterion) {
        return criteria.add(criterion);
    }

    protected Criteria addOrder(Order order) {
        return criteria.addOrder(order);
    }

    protected ProjectionList add(Projection proj) {
        return projections.add(proj);
    }

    protected ProjectionList add(Projection projection, String alias) {
        return projections.add(projection, alias);
    }

}
