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
package com.bloatit.model.demand;

import java.util.Iterator;

import com.bloatit.data.DaoDemand;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Demand;
import com.bloatit.model.lists.ListBinder;

/**
 * The Class DemandList. It is a ListBinder to transform PageIterable<DaoDemand>
 * to PageIterable<Demand>
 */
public final class DemandList extends ListBinder<Demand, DaoDemand> {

    /**
     * Instantiates a new demand list.
     * 
     * @param daoCollection the dao collection
     */
    public DemandList(final PageIterable<DaoDemand> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<Demand> createFromDaoIterator(final Iterator<DaoDemand> dao) {
        return new DemandIterator(dao);
    }

    /**
     * The Class DemandIterator is an iterator on DaoDemand that return Demands.
     */
    static final class DemandIterator extends com.bloatit.model.lists.IteratorBinder<Demand, DaoDemand> {

        /**
         * Instantiates a new demand iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public DemandIterator(final Iterable<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new demand iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public DemandIterator(final Iterator<DaoDemand> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected Demand createFromDao(final DaoDemand dao) {
            return DemandImplementation.create(dao);
        }

    }

}
