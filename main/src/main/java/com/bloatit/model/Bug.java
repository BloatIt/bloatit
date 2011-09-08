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

import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RgtBug;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPublicAccessException;
import com.bloatit.model.visitor.ModelClassVisitor;

/**
 * This is a bug report. A bug report is associated with a milestone. it is
 * quite similar to the bug report in a classical bugTracker.
 * 
 * @author Thomas Guyard
 */
public final class Bug extends UserContent<DaoBug> implements Commentable {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoBug, Bug> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public Bug doCreate(final DaoBug dao) {
            return new Bug(dao);
        }
    }

    /**
     * Find a bug in the cache or create an new one.
     * 
     * @param dao the dao
     * @return null if dao is null. Else return the new Bug.
     */
    @SuppressWarnings("synthetic-access")
    public static Bug create(final DaoBug dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new bug.
     * 
     * @param dao the dao
     */
    private Bug(final DaoBug dao) {
        super(dao);
    }

    /**
     * Create a new Bug.
     * 
     * @param member is the author of the bug.
     * @param milestone is the milestone on which this bug has been set.
     * @param title is the title of the bug.
     * @param description is a complete description of the bug.
     * @param locale is the language in which this description has been written.
     * @param errorLevel is the estimated level of the bug. see {@link Level}.
     */
    Bug(final Member member,
        final Team team,
        final Milestone milestone,
        final String title,
        final String description,
        final Locale locale,
        final Level errorLevel) {
        super(DaoBug.createAndPersist(member.getDao(), DaoGetter.get(team), milestone.getDao(), title, description, locale, errorLevel));
    }

    /**
     * Sets the error level.
     * 
     * @param level the new error level
     * @throws UnauthorizedPublicAccessException
     * @see com.bloatit.data.DaoBug#setErrorLevel(com.bloatit.data.DaoBug.Level)
     */
    public void setErrorLevel(final Level level) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtBug.ErrorLevel(), Action.WRITE);
        getDao().setErrorLevel(level);
    }

    /**
     * Sets the bug to resolved.
     * 
     * @throws UnauthorizedPublicAccessException
     * @see com.bloatit.data.DaoBug#setResolved()
     */
    public void setResolved() throws UnauthorizedPublicAccessException {
        tryAccess(new RgtBug.Status(), Action.WRITE);
        getDao().setResolved();
    }

    /**
     * Sets the bug to developing.
     * 
     * @throws UnauthorizedPublicAccessException
     * @see com.bloatit.data.DaoBug#setDeveloping()
     */
    public void setDeveloping() throws UnauthorizedPublicAccessException {
        tryAccess(new RgtBug.Status(), Action.WRITE);
        getDao().setDeveloping();
    }

    /**
     * Adds the comment.
     * 
     * @see com.bloatit.data.DaoBug#addComment(com.bloatit.data.DaoComment)
     */
    @Override
    public Comment addComment(final String text) throws UnauthorizedOperationException {
        tryAccess(new RgtBug.Comment(), Action.WRITE);
        final DaoComment comment = DaoComment.createAndPersist(this.getDao(),
                                                               DaoGetter.get(AuthToken.getAsTeam()),
                                                               AuthToken.getMember().getDao(),
                                                               text);
        getDao().addComment(comment);
        return Comment.create(comment);
    }

    @Override
    protected void delete(boolean delOrder) throws UnauthorizedOperationException {
        // Delete all comments from the bug
        for (final Comment comment : getComments()) {
            comment.delete(delOrder);
        }
        super.delete(delOrder);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the member assigned to this bug. It is always the offer author.
     * 
     * @return the assigned to
     * @see com.bloatit.data.DaoBug#getAssignedTo()
     */
    // no right management: this is public data
    public Member getAssignedTo() {
        return Member.create(getDao().getAssignedTo());
    }

    /**
     * Gets this bug description.
     * 
     * @return the description
     * @see com.bloatit.data.DaoBug#getDescription()
     */
    // no right management: this is public data
    public String getDescription() {
        return getDao().getDescription();
    }

    public void setDescription(final String description, final Member author) throws UnauthorizedOperationException {
        tryAccess(new RgtBug.Description(), Action.WRITE);
        getDao().setDescription(description, author.getDao());
    }

    /**
     * Gets the locale in which this bug is written.
     * 
     * @return the locale
     * @see com.bloatit.data.DaoBug#getLocale()
     */
    // no right management: this is public data
    public Locale getLocale() {
        return getDao().getLocale();
    }

    /**
     * Gets the error level.
     * 
     * @return the error level
     * @see com.bloatit.data.DaoBug#getErrorLevel()
     */
    // no right management: this is public data
    public Level getErrorLevel() {
        return getDao().getErrorLevel();
    }

    /**
     * Gets the milestone on which this bug has been added.
     * 
     * @return the milestone
     * @see com.bloatit.data.DaoBug#getMilestone()
     */
    // no right management: this is public data
    public Milestone getMilestone() {
        return Milestone.create(getDao().getMilestone());
    }

    /**
     * Gets the state.
     * 
     * @return the state
     * @see com.bloatit.data.DaoBug#getState()
     */
    // no right management: this is public data
    public BugState getState() {
        return getDao().getState();
    }

    /**
     * Gets the comments on that bug.
     * 
     * @return the comments
     * @see com.bloatit.data.DaoBug#getComments()
     */
    // no right management: this is public data
    public PageIterable<Comment> getComments() {
        return new CommentList(getDao().getComments());
    }

    /**
     * Gets the last update date.
     * 
     * @return the last update date
     */
    // no right management: this is public data
    public Date getLastUpdateDate() {
        final DaoComment lastComment = getDao().getLastComment();
        if (lastComment == null) {
            return getCreationDate();
        }
        return lastComment.getCreationDate();
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    // no right management: this is public data
    public String getTitle() {
        return getDao().getTitle();
    }

    // no right management: this is public data
    public Feature getFeature() {
        return getMilestone().getOffer().getFeature();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Can ...
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Tells if the authenticated user can access the <i>ErrorLevel</i>
     * property.
     * 
     * @param action the type of access you want to do on the <i>ErrorLevel</i>
     *            property.
     * @return true if you can access the <i>ErrorLevel</i> property.
     */
    public final boolean canAccessErrorLevel(final Action action) {
        return canAccess(new RgtBug.ErrorLevel(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>Status</i> property.
     * 
     * @param action the type of access you want to do on the <i>Status</i>
     *            property.
     * @return true if you can access the <i>Status</i> property.
     */
    public final boolean canAccessStatus(final Action action) {
        return canAccess(new RgtBug.Status(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>Comment</i> property.
     * 
     * @param action the type of access you want to do on the <i>Comment</i>
     *            property.
     * @return true if you can access the <i>Comment</i> property.
     */
    public final boolean canAccessComment(final Action action) {
        return canAccess(new RgtBug.Comment(), action);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
