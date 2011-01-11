package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.BankTransaction;
import com.bloatit.model.data.DaoBankTransaction;

public final class BankTransactionList extends ListBinder<BankTransaction, DaoBankTransaction> {

    public BankTransactionList(final PageIterable<DaoBankTransaction> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<BankTransaction> createFromDaoIterator(final Iterator<DaoBankTransaction> dao) {
        return new BankTransactionIterator(dao);
    }

}
