package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Comment;
import com.bloatit.model.data.DaoComment;

public final class CommentList extends ListBinder<Comment, DaoComment> {

    public CommentList(final PageIterable<DaoComment> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Comment> createFromDaoIterator(final Iterator<DaoComment> dao) {
        return new CommentIterator(dao);
    }

    static final class CommentIterator extends com.bloatit.framework.lists.IteratorBinder<Comment, DaoComment> {

        public CommentIterator(final Iterable<DaoComment> daoIterator) {
            super(daoIterator);
        }

        public CommentIterator(final Iterator<DaoComment> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Comment createFromDao(final DaoComment dao) {
            return Comment.create(dao);
        }

    }

}
