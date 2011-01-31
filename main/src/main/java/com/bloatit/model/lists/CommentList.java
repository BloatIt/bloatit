package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Comment;

public final class CommentList extends ListBinder<Comment, DaoComment> {

    public CommentList(final PageIterable<DaoComment> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Comment> createFromDaoIterator(final Iterator<DaoComment> dao) {
        return new CommentIterator(dao);
    }

    static final class CommentIterator extends com.bloatit.model.lists.IteratorBinder<Comment, DaoComment> {

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
