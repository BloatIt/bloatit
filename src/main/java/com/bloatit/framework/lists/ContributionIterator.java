package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.model.Contribution;
import com.bloatit.model.data.DaoContribution;

public class ContributionIterator extends com.bloatit.framework.lists.IteratorBinder<Contribution, DaoContribution> {

    public ContributionIterator(Iterable<DaoContribution> daoIterator) {
        super(daoIterator);
    }

    public ContributionIterator(Iterator<DaoContribution> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Contribution createFromDao(DaoContribution dao) {
        return new Contribution(dao);
    }

}
