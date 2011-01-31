package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.BankTransaction;

public final class BankTransactionList extends ListBinder<BankTransaction, DaoBankTransaction> {

    public BankTransactionList(final PageIterable<DaoBankTransaction> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<BankTransaction> createFromDaoIterator(final Iterator<DaoBankTransaction> dao) {
        return new BankTransactionIterator(dao);
    }

    static final class BankTransactionIterator extends IteratorBinder<BankTransaction, DaoBankTransaction> {

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

}
