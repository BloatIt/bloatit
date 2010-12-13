package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Transaction;
import com.bloatit.model.data.DaoTransaction;

public class TransactionList extends ListBinder<Transaction, DaoTransaction> {

    public TransactionList(final PageIterable<DaoTransaction> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Transaction> createFromDaoIterator(final Iterator<DaoTransaction> dao) {
        return new TransactionIterator(dao);
    }

}
