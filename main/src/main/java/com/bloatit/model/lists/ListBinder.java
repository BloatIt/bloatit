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

/**
 * The Class ListBinder is the base class of all the binder lists. A Binder list
 * transform a PageIterable<Dao...> to a PageIterable<...> (the same but without
 * the Dao)
 * 
 * @param <E> the Model level representation of a Dao class
 * @param <DAO> the Dao class corresponding to <code>E</code>
 */
public abstract class ListBinder<E, DAO> implements PageIterable<E> {

    /** The dao collection. */
    private final PageIterable<DAO> daoCollection;

    /**
     * Instantiates a new list binder.
     * 
     * @param daoCollection the dao collection
     */
    public ListBinder(final PageIterable<DAO> daoCollection) {
        super();
        this.daoCollection = daoCollection;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<E> iterator() {
        return createFromDaoIterator(daoCollection.iterator());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#setPage(int)
     */
    @Override
    public final void setPage(final int page) {
        daoCollection.setPage(page);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#setPageSize(int)
     */
    @Override
    public final void setPageSize(final int pageSize) {
        daoCollection.setPageSize(pageSize);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#getPageSize()
     */
    @Override
    public final int getPageSize() {
        return daoCollection.getPageSize();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#size()
     */
    @Override
    public final int size() {
        return daoCollection.size();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#pageNumber()
     */
    @Override
    public final int pageNumber() {
        return daoCollection.pageNumber();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#getCurrentPage()
     */
    @Override
    public final int getCurrentPage() {
        return daoCollection.getCurrentPage();
    }

    /**
     * Creates the from dao iterator.
     * 
     * @param dao the dao
     * @return the iterator
     */
    protected abstract Iterator<E> createFromDaoIterator(Iterator<DAO> dao);

}
