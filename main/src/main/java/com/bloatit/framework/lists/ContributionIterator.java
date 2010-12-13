package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Contribution;
import com.bloatit.model.data.DaoContribution;

public class ContributionIterator extends com.bloatit.framework.lists.IteratorBinder<Contribution, DaoContribution> {

    public ContributionIterator(final Iterable<DaoContribution> daoIterator) {
        super(daoIterator);
    }

    public ContributionIterator(final Iterator<DaoContribution> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Contribution createFromDao(final DaoContribution dao) {
        return new Contribution(dao);
    }

}
