package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.model.Comment;
import com.bloatit.model.data.DaoComment;

public class CommentList extends ListBinder<Comment, DaoComment> {

    public CommentList(PageIterable<DaoComment> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Comment> createFromDaoIterator(Iterator<DaoComment> dao) {
        return new CommentIterator(dao);
    }

}
