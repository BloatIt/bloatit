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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.framework.utils.PageIterable;

/**
 * The Class DaoAbstractQuery factor some useful methods for
 * <code>Criteria</code> based query on <code>DaoIdentifiable</code>.
 * 
 * @param <T> the Dao class returned by this query
 */
public abstract class DaoAbstractQuery<T extends DaoIdentifiable> {

    /** The criteria. */
    private final Criteria criteria;

    /** The projections to apply on the <code>criteria</code>. */
    private final ProjectionList projections = Projections.projectionList();

    /**
     * The Enum OrderType.
     */
    public enum OrderType {
        /** SQL ascending order. */
        ASC,
        /** SQL Descending order. */
        DESC
    }

    /**
     * The Enum Comparator.
     */
    public enum Comparator {

        /** == */
        EQUAL,
        /** < */
        LESS,
        /** > */
        GREATER,
        /** <= */
        LESS_EQUAL,
        /** >= */
        GREATER_EQUAL
    }

    /**
     * Instantiates a new dao abstract query.
     * 
     * @param criteria the criteria
     */
    public DaoAbstractQuery(final Criteria criteria) {
        super();
        this.criteria = criteria;
    }

    /**
     * Creates the {@link PageIterable} resulting from this query.
     * 
     * @return the {@link PageIterable}
     */
    public final PageIterable<T> createCollection() {
        prepareCriteria();
        return new CriteriaCollection<T>(criteria);
    }

    /**
     * If you are sure this query will return a unique result.
     * 
     * @return the result of this query
     */
    @SuppressWarnings("unchecked")
    public final T uniqueResult() {
        prepareCriteria();
        return (T) criteria.uniqueResult();
    }

    /**
     * Prepare the criteria (adding projection if needed).
     */
    private void prepareCriteria() {
        if (projections.getLength() > 0) {
            criteria.setProjection(projections);
        }
    }

    /**
     * Creates a criterion on the size of a collection.
     * <p>
     * For example: <code>
     * createSizeCriterion(GREATER, &quot;offers&quot;, 12)
     * </code> Will select elements which has more than 12 offers associated
     * with.
     * </p>
     * 
     * @param cmp the comparator
     * @param element the element name. It must be a collection (a mapped
     *            association).
     * @param nb the number on which we compare the size of the collection
     * @return the criterion
     */
    protected final Criterion createSizeCriterion(final Comparator cmp, final String element, final int nb) {
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

    /**
     * Creates a criterion on a {@link Comparable} element.
     * <p>
     * For example: <code>
     * createNbRestriction(GREATER, &quot;amount&quot;, 12)
     * </code> Will select elements with amount > 12.
     * </p>
     * 
     * @param cmp the comparator
     * @param element the countable element name.
     * @param nb the number on which we try to compare the <code>element</code>
     *            value.
     * @return the criterion
     */
    protected final Criterion createNbCriterion(final Comparator cmp, final String element, final Object nb) {
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

    /**
     * Adds the <code>criterion</code> on the current Criteria query.
     * 
     * @param criterion the criterion
     * @return the criteria
     */
    protected final Criteria add(final Criterion criterion) {
        return criteria.add(criterion);
    }

    /**
     * Adds the <code>order</code> on the current Criteria query.
     * 
     * @param order the order
     * @return the criteria
     */
    protected final Criteria addOrder(final Order order) {
        return criteria.addOrder(order);
    }

    /**
     * Adds the projection on the current Criteria query. All the projections
     * will be add at the end of the criteria in a {@link ProjectionList}.
     * 
     * @param proj the {@link Projection}
     * @return the projection list
     * @see ProjectionList#add(Projection)
     */
    protected final ProjectionList add(final Projection proj) {
        return projections.add(proj);
    }

    /**
     * /** Adds the projection on the current Criteria query. All the
     * projections will be add at the end of the criteria in a
     * {@link ProjectionList}.
     * 
     * @param projection the projection
     * @param alias the alias
     * @return the projection list
     * @see ProjectionList#add(Projection, String)
     */
    protected final ProjectionList add(final Projection projection, final String alias) {
        return projections.add(projection, alias);
    }

}
