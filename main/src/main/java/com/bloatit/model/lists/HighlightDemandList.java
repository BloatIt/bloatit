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

import com.bloatit.data.DaoHighlightDemand;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.HighlightDemand;

/**
 * The Class HighlightDemandList transforms PageIterable<DaoHighlightDemand> to
 * PageIterable<HighlightDemand>.
 */
public final class HighlightDemandList extends ListBinder<HighlightDemand, DaoHighlightDemand> {

    /**
     * Instantiates a new project list.
     * 
     * @param daoCollection the dao collection
     */
    public HighlightDemandList(final PageIterable<DaoHighlightDemand> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator)
     */
    @Override
    protected Iterator<HighlightDemand> createFromDaoIterator(final Iterator<DaoHighlightDemand> dao) {
        return new HighlightDemandIterator(dao);
    }

    /**
     * The Class ProjectIterator.
     */
    static final class HighlightDemandIterator extends IteratorBinder<HighlightDemand, DaoHighlightDemand> {

        /**
         * Instantiates a new project iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public HighlightDemandIterator(final Iterable<DaoHighlightDemand> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new project iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public HighlightDemandIterator(final Iterator<DaoHighlightDemand> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object)
         */
        @Override
        protected HighlightDemand createFromDao(final DaoHighlightDemand dao) {
            return HighlightDemand.create(dao);
        }

    }

}
