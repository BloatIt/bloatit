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

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.bloatit.framework.utils.PageIterable;

abstract class DaoAbstractListFactory<T extends DaoIdentifiable> {
    private final Criteria criteria;
    private final ProjectionList projections = Projections.projectionList();

    public enum OrderType {
        ASC, DESC
    }

    public enum Comparator {
        EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL
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

    protected Criterion createSizeCriterion(Comparator cmp, String element, int nb) {
        switch (cmp) {
        case EQUAL:
            return Restrictions.sizeEq(element, nb);
        case LESS:
            return Restrictions.sizeLt(element, nb);
        case LESS_EQUAL:
            return Restrictions.sizeLe(element, nb);
        case GREATER:
            return Restrictions.sizeGt(element, nb);
        case GREATER_EQUAL:
            return Restrictions.sizeGe(element, nb);
        default:
            return Restrictions.sizeEq(element, nb);
        }
    }

    protected Criterion createNbCriterion(Comparator cmp, String element, Object nb) {
        switch (cmp) {
        case EQUAL:
            return Restrictions.eq(element, nb);
        case LESS:
            return Restrictions.lt(element, nb);
        case LESS_EQUAL:
            return Restrictions.le(element, nb);
        case GREATER:
            return Restrictions.gt(element, nb);
        case GREATER_EQUAL:
            return Restrictions.ge(element, nb);
        default:
            return Restrictions.eq(element, nb);
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
