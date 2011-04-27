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
package com.bloatit.data.search;

import java.util.Iterator;

import org.hibernate.search.FullTextQuery;

import com.bloatit.framework.utils.PageIterable;

/**
 * This is the {@link PageIterable} implementation using the Hibernate Search
 * querying interface.
 * 
 * @param <T> the class stored in this collection
 */
public class SearchCollection<T> implements PageIterable<T> {

    private final FullTextQuery query;
    private int pageSize;
    private int currentPage;

    protected SearchCollection(final FullTextQuery query) {
        super();
        this.query = query;
        this.pageSize = 0;
        this.currentPage = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return query.iterate();
    }

    @Override
    public void setPageSize(final int pageSize) {
        query.setFetchSize(pageSize);
        query.setMaxResults(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int size() {
        return query.getResultSize();
    }

    @Override
    public int pageNumber() {
        if (pageSize != 0) {
            // make sure every element is in a page :
            // round to superior.
            return size() / pageSize + (size() % pageSize != 0 ? 1 : 0);
        }
        return 1;
    }

    @Override
    public void setPage(final int page) {
        currentPage = page;
        query.setFirstResult(page * pageSize);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

}
