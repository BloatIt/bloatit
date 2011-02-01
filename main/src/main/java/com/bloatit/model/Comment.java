package com.bloatit.model;

import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;

/**
 * @see DaoComment
 */
public final class Comment extends Kudosable<DaoComment> {

    /**
     * Create a new comment and return it. It return null if the <code>dao</code> is null.
     */
    public static Comment create(final DaoComment dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoComment> created = CacheManager.get(dao);
            if (created == null) {
                return new Comment(dao);
            }
            return (Comment) created;
        }
        return null;
    }

    private Comment(final DaoComment dao) {
        super(dao);
    }

    /**
     * Return all the children comment of this comment.
     *
     * @see DaoComment#getChildren()
     */
    public PageIterable<Comment> getChildren() {
        return new CommentList(getDao().getChildren());
    }

    /**
     * @param text is the comment text.
     * @see #addChildComment(Comment)
     */
    public void addChildComment(final String text) {
        getDao().addChildComment(DaoComment.createAndPersist(getAuthToken().getMember().getDao(), text));
    }

    /**
     * @return the text of this comment.
     */
    public String getText() {
        return getDao().getText();
    }

    /**
     * Add a comment to the list of children of this comment.
     *
     * @see #addChildComment(String)
     */
    public void addChildComment(final Comment comment) {
        getDao().addChildComment(comment.getDao());
    }

    /**
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return KudosableConfiguration.getCommentTurnPending();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return KudosableConfiguration.getCommentTurnValid();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return KudosableConfiguration.getCommentTurnRejected();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return KudosableConfiguration.getCommentTurnHidden();
    }

}
