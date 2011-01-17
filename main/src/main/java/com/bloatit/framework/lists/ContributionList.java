package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Contribution;
import com.bloatit.model.data.DaoContribution;

public final class ContributionList extends ListBinder<Contribution, DaoContribution> {

    public ContributionList(final PageIterable<DaoContribution> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Contribution> createFromDaoIterator(final Iterator<DaoContribution> dao) {
        return new ContributionIterator(dao);
    }

    static final class ContributionIterator extends com.bloatit.framework.lists.IteratorBinder<Contribution, DaoContribution> {

        public ContributionIterator(final Iterable<DaoContribution> daoIterator) {
            super(daoIterator);
        }

        public ContributionIterator(final Iterator<DaoContribution> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Contribution createFromDao(final DaoContribution dao) {
            return Contribution.create(dao);
        }

    }

}
