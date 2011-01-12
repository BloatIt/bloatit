package com.bloatit.framework.managers;

import com.bloatit.framework.Comment;
import com.bloatit.model.data.DBRequests;
import com.bloatit.model.data.DaoComment;

public final class CommentManager {

    private CommentManager(){
        // Desactivate default ctor
    }

    public static Comment getCommentById(final Integer id) {
        return Comment.create(DBRequests.getById(DaoComment.class, id));
    }
}
