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

import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.utils.PageIterable;

/**
 * This is the implementation of the {@link PageIterable} interface using a
 * Hibernate HQL query.
 */
public class QueryCollection<T> implements PageIterable<T> {

    private final Query query;
    private final Query sizeQuery;
    private int pageSize;
    private int size;
    private int currentPage;

    /**
     * Create a {@link QueryCollection} using a named query. This constructor
     * will look for <b>two</b> named queries:
     * <ul>
     * <li>The query named: <code>nameQuery</code></li>
     * <li>The query named: <code>nameQuery + ".size"</code>, for the size of
     * the collection.</li>
     * </ul>
     * This should not be a problem if you follow the naming convention for
     * queries.
     * <p>
     * If One of the two queries is missing this will throws an
     * {@link HibernateException}.
     * </p>
     * 
     * @param nameQuery the name of the query to find the collection. The query
     *            named <code>nameQuery + ".size"</code> <b>must</b> exists.
     */
    public QueryCollection(String nameQuery) {
        this(SessionManager.getNamedQuery(nameQuery), SessionManager.getNamedQuery(nameQuery + ".size"));
    }

    public QueryCollection(Query query, Query sizeQuery) {
        this.pageSize = 0;
        this.size = -1;
        this.query = query;
        this.sizeQuery = sizeQuery;
    }

    public QueryCollection<T> setEntity(String name, Object entity) {
        query.setEntity(name, entity);
        sizeQuery.setEntity(name, entity);
        return this;
    }

    public QueryCollection<T> setParameter(String name, Object entity) {
        query.setParameter(name, entity);
        sizeQuery.setParameter(name, entity);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return query.list().iterator();
    }

    @Override
    public void setPage(int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    @Override
    public void setPageSize(int pageSize) {
        query.setMaxResults(pageSize);
        query.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int size() {
        if (size == -1) {
            size = ((Long) sizeQuery.uniqueResult()).intValue();
            return size;
        }
        return size;
    }

    @Override
    public int pageNumber() {
        if (pageSize != 0) {
            return (int) Math.ceil((double) size() / (double) pageSize);
        }
        return 1;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }
}
