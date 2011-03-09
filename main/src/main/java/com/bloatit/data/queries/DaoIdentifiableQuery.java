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

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.SessionManager;

/**
 * A query on DaoIdentifiable using hibernate Criteria query.
 *
 * @param <T> the generic type representing a concrete Dao class.
 */
public class DaoIdentifiableQuery<T extends DaoIdentifiable> extends DaoAbstractQuery<T> {

    /**
     * Instantiates a new dao identifiable list factory.
     *
     * @param criteria the criteria
     */
    protected DaoIdentifiableQuery(final Criteria criteria) {
        super(criteria);
    }

    /**
     * Instantiates a new dao identifiable list factory.
     */
    public DaoIdentifiableQuery() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoIdentifiable.class));
    }

    /**
     * Add a WHERE close to have only the identifiable with the id
     * <code>id</code>
     *
     * @param id the id
     */
    public void idEquals(final Integer id) {
        add(Restrictions.eq("id", id));
    }

    /**
     * Add an Order by close to this query. Use this with caution, if the column
     * parameter does not represent a valid <code>column</code> on this
     * criteria, the method will throw a HibernateException.
     *
     * @param column the column name. Think about possible aliases (see
     *            {@link Criteria#createAlias(String, String)}).
     * @param order the order ...
     */
    public void orderBy(final String column, final OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(column));
        } else {
            addOrder(Order.desc(column));
        }
    }
}
