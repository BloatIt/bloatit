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
package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.framework.utils.PageIterable;

public abstract class ListBinder<E, DAO> implements PageIterable<E> {

    private final PageIterable<DAO> daoCollection;

    public ListBinder(final PageIterable<DAO> daoCollection) {
        super();
        this.daoCollection = daoCollection;
    }

    @Override
    public final Iterator<E> iterator() {
        return createFromDaoIterator(daoCollection.iterator());
    }

    @Override
    public final void setPage(final int page) {
        daoCollection.setPage(page);
    }

    @Override
    public final void setPageSize(final int pageSize) {
        daoCollection.setPageSize(pageSize);
    }

    @Override
    public final int getPageSize() {
        return daoCollection.getPageSize();
    }

    @Override
    public final int size() {
        return daoCollection.size();
    }

    @Override
    public final int pageNumber() {
        return daoCollection.pageNumber();
    }

    @Override
    public final int getCurrentPage() {
        return daoCollection.getCurrentPage();
    }

    protected abstract Iterator<E> createFromDaoIterator(Iterator<DAO> dao);

}
