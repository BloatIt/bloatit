package com.bloatit.framework;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoKudosable;

public final class Comment extends Kudosable {

    private DaoComment dao;

    public String getText() {
        return dao.getText();
    }

    public static Comment create(final DaoComment dao) {
        if (dao == null) {
            return null;
        }
        return new Comment(dao);
    }

    private Comment(final DaoComment dao) {
        super();
        this.dao = dao;
    }

    public PageIterable<Comment> getChildren() {
        return new CommentList(dao.getChildrenFromQuery());
    }
    
    public void addChildComment(final String text) {
        dao.addChildComment(DaoComment.createAndPersist(getAuthToken().getMember().getDao(), text));
    }

    // TODO make sure it still works
    public void addChildComment(final Comment comment) {
        dao.addChildComment(comment.getDao());
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
