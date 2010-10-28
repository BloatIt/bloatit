package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Transaction;
import com.bloatit.model.data.DaoTransaction;

public class TransactionIterator extends com.bloatit.framework.lists.IteratorBinder<Transaction, DaoTransaction> {

    public TransactionIterator(Iterable<DaoTransaction> daoIterator) {
        super(daoIterator);
    }

    public TransactionIterator(Iterator<DaoTransaction> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Transaction createFromDao(DaoTransaction dao) {
        return new Transaction(dao);
    }

}
