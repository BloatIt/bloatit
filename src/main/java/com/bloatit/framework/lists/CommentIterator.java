package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Comment;
import com.bloatit.model.data.DaoComment;

public class CommentIterator extends com.bloatit.framework.lists.IteratorBinder<Comment, DaoComment> {

    public CommentIterator(Iterable<DaoComment> daoIterator) {
        super(daoIterator);
    }

    public CommentIterator(Iterator<DaoComment> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Comment createFromDao(DaoComment dao) {
        return new Comment(dao);
    }

}
