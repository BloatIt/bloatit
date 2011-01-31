package com.bloatit.model;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoKudosable;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;

/**
 * @see DaoComment
 */
public final class Comment extends Kudosable {

    private final DaoComment dao;

    /**
     * Create a new comment and return it. It return null if the <code>dao</code> is null.
     */
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

    /**
     * Return all the children comment of this comment.
     * 
     * @see DaoComment#getChildren()
     */
    public PageIterable<Comment> getChildren() {
        return new CommentList(dao.getChildren());
    }

    /**
     * @param text is the comment text.
     * @see #addChildComment(Comment)
     */
    public void addChildComment(final String text) {
        dao.addChildComment(DaoComment.createAndPersist(getAuthToken().getMember().getDao(), text));
    }

    /**
     * @return the text of this comment.
     */
    public String getText() {
        return dao.getText();
    }

    /**
     * Add a comment to the list of children of this comment.
     * 
     * @see #addChildComment(String)
     */
    public void addChildComment(final Comment comment) {
        dao.addChildComment(comment.getDao());
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

    protected DaoComment getDao() {
        return dao;
    }

}
