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

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.bloatit.framework.utils.PageIterable;

/**
 * A mapped list is a persistant list mapped to a {@link PageIterable}. You can
 * use it when you have a list that you want to paginate.
 * 
 * @author Thomas Guyard
 * @param <T> the object stored in the list.
 */
public class MappedList<T> implements PageIterable<T> {

    private final List<T> list;
    private final Query sizeQuery;
    private int pageSize;
    private int size;
    private int currentPage;

    /**
     * Create a new mapped list on the list mappedList. The default page size is
     * the whole list. The size of the list will be calculate using a filter
     * ("select count(*)").
     * 
     * @param mappedList
     */
    protected MappedList(final List<T> mappedList) {
        this.list = mappedList;
        this.sizeQuery = SessionManager.createFilter(mappedList, "select count(*)");
        this.pageSize = 0;
        this.size = -1;
    }

    @Override
    public Iterator<T> iterator() {
        return this.list.listIterator(this.pageSize * this.currentPage);
    }

    @Override
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int size() {
        if (this.size == -1) {
            this.size = ((Long) this.sizeQuery.uniqueResult()).intValue();
            return this.size;
        }
        return this.size;
    }

    @Override
    public int pageNumber() {
        if (this.pageSize != 0) {
            return (int) Math.ceil((double) size() / (double) this.pageSize);
        }
        return 1;
    }

    @Override
    public void setPage(final int page) {
        this.currentPage = page;
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }

}
