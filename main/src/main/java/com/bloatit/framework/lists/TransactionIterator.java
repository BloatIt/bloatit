package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Transaction;
import com.bloatit.model.data.DaoTransaction;

public final class TransactionIterator extends com.bloatit.framework.lists.IteratorBinder<Transaction, DaoTransaction> {

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
