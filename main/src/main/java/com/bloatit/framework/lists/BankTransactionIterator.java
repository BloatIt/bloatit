package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.BankTransaction;
import com.bloatit.model.data.DaoBankTransaction;

public final class BankTransactionIterator extends IteratorBinder<BankTransaction, DaoBankTransaction> {

    public BankTransactionIterator(final Iterable<DaoBankTransaction> daoIterator) {
        super(daoIterator);
    }

    public BankTransactionIterator(final Iterator<DaoBankTransaction> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected BankTransaction createFromDao(final DaoBankTransaction dao) {
        return BankTransaction.create(dao);
    }

}
