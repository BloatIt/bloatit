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

import com.bloatit.data.DaoBatch;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Batch;

/**
 * The Class BatchList transforms PageIterable<DaoBatch> to PageIterable<Batch>.
 */
public final class BatchList extends ListBinder<Batch, DaoBatch> {

    /**
     * Instantiates a new batch list.
     * 
     * @param daoCollection the dao collection
     */
    public BatchList(final PageIterable<DaoBatch> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator)
     */
    @Override
    protected Iterator<Batch> createFromDaoIterator(final Iterator<DaoBatch> dao) {
        return new BatchIterator(dao);
    }

    /**
     * The Class BatchIterator takes an iterator on a DaoBatch and return one on a Batch.
     */
    static final class BatchIterator extends com.bloatit.model.lists.IteratorBinder<Batch, DaoBatch> {

        /**
         * Instantiates a new batch iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public BatchIterator(final Iterable<DaoBatch> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new batch iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public BatchIterator(final Iterator<DaoBatch> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object)
         */
        @Override
        protected Batch createFromDao(final DaoBatch dao) {
            return Batch.create(dao);
        }

    }

}
