package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Contribution;
import com.bloatit.model.data.DaoContribution;

public class ContributionList extends ListBinder<Contribution, DaoContribution> {

    public ContributionList(final PageIterable<DaoContribution> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Contribution> createFromDaoIterator(final Iterator<DaoContribution> dao) {
        return new ContributionIterator(dao);
    }

}
