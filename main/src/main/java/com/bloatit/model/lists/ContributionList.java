//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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
