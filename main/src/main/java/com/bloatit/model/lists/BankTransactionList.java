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

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.BankTransaction;

/**
 * The Class BankTransactionList transforms PageIterable<DaoBankTransaction> to
 * PageIterable<BankTransaction>.
 */
public final class BankTransactionList extends ListBinder<BankTransaction, DaoBankTransaction> {

    /**
     * Instantiates a new bank transaction list.
     * 
     * @param daoCollection the dao collection
     */
    public BankTransactionList(final PageIterable<DaoBankTransaction> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<BankTransaction> createFromDaoIterator(final Iterator<DaoBankTransaction> dao) {
        return new BankTransactionIterator(dao);
    }

    /**
     * The Class BankTransactionIterator take an Iterator on a
     * DaoBankTransaction and return one on a BankTransaction.
     */
    static final class BankTransactionIterator extends IteratorBinder<BankTransaction, DaoBankTransaction> {

        /**
         * Instantiates a new bank transaction iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public BankTransactionIterator(final Iterable<DaoBankTransaction> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new bank transaction iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public BankTransactionIterator(final Iterator<DaoBankTransaction> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected BankTransaction createFromDao(final DaoBankTransaction dao) {
            return BankTransaction.create(dao);
        }

    }

}
