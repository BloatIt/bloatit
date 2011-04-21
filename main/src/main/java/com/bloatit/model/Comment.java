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
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.feature.FeatureImplementation;
import com.bloatit.model.lists.CommentList;

/**
 * The Class Comment.
 *
 * @see DaoComment
 */
public final class Comment extends Kudosable<DaoComment> implements Commentable {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoComment, Comment> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public Comment doCreate(final DaoComment dao) {
            return new Comment(dao);
        }
    }

    /**
     * Create a new comment and return it. It return null if the
     * <code>dao</code> is null.
     *
     * @param dao the dao
     * @return the comment or null.
     */
    @SuppressWarnings("synthetic-access")
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
        return ModelConfiguration.getKudosableCommentTurnPending();
    }

    /**
     * Turn valid.
     *
     * @return the int
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return ModelConfiguration.getKudosableCommentTurnValid();
    }

    /**
     * Turn rejected.
     *
     * @return the int
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return ModelConfiguration.getKudosableCommentTurnRejected();
    }

    /**
     * Turn hidden.
     *
     * @return the int
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return ModelConfiguration.getKudosableCommentTurnHidden();
    }

    /**
     * Adds the comment.
     *
     * @see com.bloatit.data.DaoBug#addComment(com.bloatit.data.DaoComment)
     */
    @Override
    public Comment addComment(final String text) throws UnauthorizedOperationException {
        // TODO: access right
        // tryAccess(new BugRight.Comment(), Action.WRITE);
        final DaoComment comment = DaoComment.createAndPersist(this.getDao(),
                                                               DaoGetter.getTeam(getAuthToken().getAsTeam()),
                                                               getAuthToken().getMember().getDao(),
                                                               text);
        getDao().addChildComment(comment);
        return Comment.create(comment);
    }

    public enum ParentType {
        COMMENT, FEATURE, BUG, RELEASE
    }

    public ParentType getParentType() {
        if (getDao().getFatherFeature() != null) {
            return ParentType.FEATURE;
        }
        if (getDao().getFatherBug() != null) {
            return ParentType.BUG;
        }
        if (getDao().getFatherRelease() != null) {
            return ParentType.RELEASE;
        }

        return ParentType.COMMENT;
    }

    /**
     * return the type of the comment else if the parent is a comment. In this
     * case, return the root parent type of the parent comment.
     */
    public ParentType getRootParentType() {
        if (getDao().getFather() != null) {
            getParentComment().getRootParentType();
        }
        return getParentType();
    }

    public Comment getParentComment() {
        return Comment.create(getDao().getFather());
    }

    public Comment getRootComment() {
        if (getDao().getFather() != null) {
            return getParentComment().getRootComment();
        }

        return this;
    }

    public Feature getParentFeature() {
        return FeatureImplementation.create(getDao().getFatherFeature());
    }

    public Release getParentRelease() {
        return Release.create(getDao().getFatherRelease());
    }

    public Bug getParentBug() {
        return Bug.create(getDao().getFatherBug());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
