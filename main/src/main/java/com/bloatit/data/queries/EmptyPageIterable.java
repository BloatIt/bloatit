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

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.framework.utils.PageIterable;

/**
 * The Class EmptyPageIterable is a generic and always empty
 * {@link PageIterable}.
 * 
 * @param <T> the generic type.
 */
public class EmptyPageIterable<T> implements PageIterable<T> {

    /** The page size. */
    private int pageSize;

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return (Iterator<T>) Collections.emptyList().iterator();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#setPageSize(int)
     */
    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#getPageSize()
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#size()
     */
    @Override
    public int size() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#pageNumber()
     */
    @Override
    public int pageNumber() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#setPage(int)
     */
    @Override
    public void setPage(int page) {
        // nothing to do it is empty.
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.framework.utils.PageIterable#getCurrentPage()
     */
    @Override
    public int getCurrentPage() {
        return 0;
    }
}
