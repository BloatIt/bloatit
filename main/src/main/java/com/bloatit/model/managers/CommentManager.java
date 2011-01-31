package com.bloatit.model.managers;

import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoComment;
import com.bloatit.model.Comment;

public final class CommentManager {

    private CommentManager() {
        // Desactivate default ctor
    }

    public static Comment getCommentById(final Integer id) {
        return Comment.create(DBRequests.getById(DaoComment.class, id));
    }
}
