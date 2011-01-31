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
            return new Transaction(dao);
        }

    }

}
