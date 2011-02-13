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

import com.bloatit.data.DaoGroup;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;

/**
 * The Class GroupList transforms PageIterable<DaoGroup> to PageIterable<Group>.
 */
public final class GroupList extends ListBinder<Group, DaoGroup> {

    /**
     * Instantiates a new group list.
     * 
     * @param daoCollection the dao collection
     */
    public GroupList(final PageIterable<DaoGroup> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator)
     */
    @Override
    protected Iterator<Group> createFromDaoIterator(final Iterator<DaoGroup> dao) {
        return new GroupIterator(dao);
    }

    /**
     * The Class GroupIterator.
     */
    static final class GroupIterator extends com.bloatit.model.lists.IteratorBinder<Group, DaoGroup> {

        /**
         * Instantiates a new group iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public GroupIterator(final Iterable<DaoGroup> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new group iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public GroupIterator(final Iterator<DaoGroup> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object)
         */
        @Override
        protected Group createFromDao(final DaoGroup dao) {
            return Group.create(dao);
        }

    }

}
