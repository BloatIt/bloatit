//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import com.bloatit.data.DaoComment;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.right.AuthToken;

/**
 * The Class Comment.
 * 
 * @see DaoComment
 */
public final class Comment extends Kudosable<DaoComment> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate method. See the
     * base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoComment, Comment> {

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @Override
        public Comment doCreate(final DaoComment dao) {
            return new Comment(dao);
        }
    }

    /**
     * Create a new comment and return it. It return null if the <code>dao</code> is null.
     * 
     * @param dao the dao
     * @return the comment or null.
     */
    public static Comment create(final DaoComment dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new comment.
     * 
     * @param dao the dao
     */
    private Comment(final DaoComment dao) {
        super(dao);
    }

    /**
     * Adds a child comment to this comment. The author is found using the
     * {@link AuthToken}.
     * 
     * @param text is the comment text.
     * @throws UnauthorizedOperationException if the user does not have the WRITE right on
     *         the Comment property
     * @see #addChildComment(Comment) TODO: Make the authentication system.
     * @see #authenticate(AuthToken)
     */
    public void addChildComment(final String text) throws UnauthorizedOperationException {
        getDao().addChildComment(DaoComment.createAndPersist(getAuthToken().getMember().getDao(), text));
    }

    /**
     * Add a comment to the list of children of this comment.
     * 
     * @param comment the comment
     * @see #addChildComment(String)
     */
    public void addChildComment(final Comment comment) {
        getDao().addChildComment(comment.getDao());
    }

    /**
     * Return all the children comment of this comment.
     * 
     * @return the children
     * @see DaoComment#getChildren()
     */
    public PageIterable<Comment> getChildren() {
        return new CommentList(getDao().getChildren());
    }

    /**
     * Gets the text.
     * 
     * @return the text of this comment.
     */
    public String getText() {
        return getDao().getText();
    }

    /**
     * Turn pending.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return KudosableConfiguration.getCommentTurnPending();
    }

    /**
     * Turn valid.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return KudosableConfiguration.getCommentTurnValid();
    }

    /**
     * Turn rejected.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return KudosableConfiguration.getCommentTurnRejected();
    }

    /**
     * Turn hidden.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return KudosableConfiguration.getCommentTurnHidden();
    }

}
