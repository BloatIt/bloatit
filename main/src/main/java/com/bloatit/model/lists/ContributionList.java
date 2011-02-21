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

/**
 * The Class ContributionList transforms PageIterable<DaoContribution> to
 * PageIterable<Contribution>.
 */
public final class ContributionList extends ListBinder<Contribution, DaoContribution> {

    /**
     * Instantiates a new contribution list.
     * 
     * @param daoCollection the dao collection
     */
    public ContributionList(final PageIterable<DaoContribution> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<Contribution> createFromDaoIterator(final Iterator<DaoContribution> dao) {
        return new ContributionIterator(dao);
    }

    /**
     * The Class ContributionIterator.
     */
    static final class ContributionIterator extends com.bloatit.model.lists.IteratorBinder<Contribution, DaoContribution> {

        /**
         * Instantiates a new contribution iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public ContributionIterator(final Iterable<DaoContribution> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new contribution iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public ContributionIterator(final Iterator<DaoContribution> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected Contribution createFromDao(final DaoContribution dao) {
            return Contribution.create(dao);
        }

    }

}
