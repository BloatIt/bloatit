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

import com.bloatit.data.DaoTransaction;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Transaction;

/**
 * The Class TransactionList transforms PageIterable<DaoTransaction> to
 * PageIterable<Transaction>.
 */
public final class TransactionList extends ListBinder<Transaction, DaoTransaction> {

    /**
     * Instantiates a new transaction list.
     * 
     * @param daoCollection the dao collection
     */
    public TransactionList(final PageIterable<DaoTransaction> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<Transaction> createFromDaoIterator(final Iterator<DaoTransaction> dao) {
        return new TransactionIterator(dao);
    }

    /**
     * The Class TransactionIterator.
     */
    static final class TransactionIterator extends com.bloatit.model.lists.IteratorBinder<Transaction, DaoTransaction> {

        /**
         * Instantiates a new transaction iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public TransactionIterator(final Iterable<DaoTransaction> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new transaction iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public TransactionIterator(final Iterator<DaoTransaction> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected Transaction createFromDao(final DaoTransaction dao) {
            return Transaction.create(dao);
        }
    }
}
