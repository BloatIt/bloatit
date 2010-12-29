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

}
