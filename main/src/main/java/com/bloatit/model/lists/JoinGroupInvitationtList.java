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

import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.JoinGroupInvitation;

/**
 * The Class JoinGroupInvitationList transforms PageIterable<DaoJoinGroupInvitation> to PageIterable<JoinGroupInvitation>.
 */
public final class JoinGroupInvitationtList extends ListBinder<JoinGroupInvitation, DaoJoinGroupInvitation> {

    /**
     * Instantiates a new comment list.
     * 
     * @param daoCollection the dao collection
     */
    public JoinGroupInvitationtList(final PageIterable<DaoJoinGroupInvitation> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator)
     */
    @Override
    protected Iterator<JoinGroupInvitation> createFromDaoIterator(final Iterator<DaoJoinGroupInvitation> dao) {
        return new JoinGroupInvitationIterator(dao);
    }

    /**
     * The Class JoinGroupInvitationIterator.
     */
    static final class JoinGroupInvitationIterator extends com.bloatit.model.lists.IteratorBinder<JoinGroupInvitation, DaoJoinGroupInvitation> {

        /**
         * Instantiates a new comment iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public JoinGroupInvitationIterator(final Iterable<DaoJoinGroupInvitation> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new comment iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public JoinGroupInvitationIterator(final Iterator<DaoJoinGroupInvitation> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object)
         */
        @Override
        protected JoinGroupInvitation createFromDao(final DaoJoinGroupInvitation dao) {
            return JoinGroupInvitation.create(dao);
        }

    }

}
