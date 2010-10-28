package com.bloatit.framework;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoKudosable;

public class Comment extends Kudosable {

    private DaoComment dao;

    public String getText() {
        return dao.getText();
    }

    public Comment(DaoComment dao) {
        super();
        this.dao = dao;
    }

    public PageIterable<Comment> getChildren() {
        return new CommentList(dao.getChildrenFromQuery());
    }

    public void addChildComment(Comment Comment) {
        dao.getChildren().add(Comment.getDao());
    }

    protected Comment() {
        super();
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

    protected DaoComment getDao() {
        return dao;
    }

}
