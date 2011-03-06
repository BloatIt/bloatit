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

import org.hibernate.Criteria;

import com.bloatit.framework.utils.PageIterable;

/**
 * This is the implementation of the {@link PageIterable} interface using a
 * Hibernate criteria.
 */
public class CriteriaCollection<T> implements PageIterable<T> {

    private final Criteria criteria;
    private int pageSize;
    private int size;
    private int currentPage;

    protected CriteriaCollection(final Criteria criteria) {
        pageSize = 0;
        size = -1;
        this.criteria = criteria;
    }

    protected Criteria getCriteria() {
        return criteria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return criteria.list().iterator();
    }

    @Override
    public void setPage(final int page) {
        currentPage = page;
        criteria.setFirstResult(page * pageSize);
    }

    @Override
    public void setPageSize(final int pageSize) {
        criteria.setMaxResults(pageSize);
        criteria.setFetchSize(pageSize);
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int size() {
        if (size == -1) {
            size = criteria.list().size();
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
