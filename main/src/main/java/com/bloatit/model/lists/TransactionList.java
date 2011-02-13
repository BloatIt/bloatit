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

public final class TransactionList extends ListBinder<Transaction, DaoTransaction> {

    public TransactionList(final PageIterable<DaoTransaction> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Transaction> createFromDaoIterator(final Iterator<DaoTransaction> dao) {
        return new TransactionIterator(dao);
    }

    static final class TransactionIterator extends com.bloatit.model.lists.IteratorBinder<Transaction, DaoTransaction> {

        public TransactionIterator(final Iterable<DaoTransaction> daoIterator) {
            super(daoIterator);
        }

        public TransactionIterator(final Iterator<DaoTransaction> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Transaction createFromDao(final DaoTransaction dao) {
            return Transaction.create(dao);
        }

    }

}
