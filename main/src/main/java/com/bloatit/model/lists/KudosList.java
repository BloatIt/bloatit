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

import com.bloatit.data.DaoKudos;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Kudos;

/**
 * The Class KudosList transforms PageIterable<DaoKudos> to PageIterable<Kudos>.
 */
public final class KudosList extends ListBinder<Kudos, DaoKudos> {

    /**
     * Instantiates a new kudos list.
     * 
     * @param daoCollection the dao collection
     */
    public KudosList(final PageIterable<DaoKudos> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<Kudos> createFromDaoIterator(final Iterator<DaoKudos> dao) {
        return new KudosIterator(dao);
    }

    /**
     * The Class KudosIterator.
     */
    static final class KudosIterator extends com.bloatit.model.lists.IteratorBinder<Kudos, DaoKudos> {

        /**
         * Instantiates a new kudos iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public KudosIterator(final Iterable<DaoKudos> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new kudos iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public KudosIterator(final Iterator<DaoKudos> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected Kudos createFromDao(final DaoKudos dao) {
            return Kudos.create(dao);
        }

    }

}
