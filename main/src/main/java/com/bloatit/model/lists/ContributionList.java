package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoContribution;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Contribution;

public final class ContributionList extends ListBinder<Contribution, DaoContribution> {

    public ContributionList(final PageIterable<DaoContribution> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Contribution> createFromDaoIterator(final Iterator<DaoContribution> dao) {
        return new ContributionIterator(dao);
    }

    static final class ContributionIterator extends com.bloatit.model.lists.IteratorBinder<Contribution, DaoContribution> {

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
