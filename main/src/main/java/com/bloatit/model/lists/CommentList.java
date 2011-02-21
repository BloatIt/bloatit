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

import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Comment;

/**
 * The Class CommentList transforms PageIterable<DaoComment> to
 * PageIterable<Comment>.
 */
public final class CommentList extends ListBinder<Comment, DaoComment> {

    /**
     * Instantiates a new comment list.
     * 
     * @param daoCollection the dao collection
     */
    public CommentList(final PageIterable<DaoComment> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator
     * )
     */
    @Override
    protected Iterator<Comment> createFromDaoIterator(final Iterator<DaoComment> dao) {
        return new CommentIterator(dao);
    }

    /**
     * The Class CommentIterator.
     */
    static final class CommentIterator extends com.bloatit.model.lists.IteratorBinder<Comment, DaoComment> {

        /**
         * Instantiates a new comment iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public CommentIterator(final Iterable<DaoComment> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new comment iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public CommentIterator(final Iterator<DaoComment> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object
         * )
         */
        @Override
        protected Comment createFromDao(final DaoComment dao) {
            return Comment.create(dao);
        }

    }

}
