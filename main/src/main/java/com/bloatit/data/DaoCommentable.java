package com.bloatit.data;

import com.bloatit.framework.utils.PageIterable;

public interface DaoCommentable {

    /**
     * Use a HQL query to get the first level comments as a PageIterable
     * collection
     */
    PageIterable<DaoComment> getComments();

    DaoComment getLastComment();

    void addComment(DaoComment comment);
}
