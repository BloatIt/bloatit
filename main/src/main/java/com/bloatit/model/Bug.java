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
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoBug.State;
import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;

/**
 * This is a bug report. A bug report is associated with a batch. it is quite
 * similar to the bug report in a classical bugTracker.
 * 
 * @author Thomas Guyard
 */
public class Bug extends UserContent<DaoBug> {

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
     * @param batch is the batch on which this bug has been set.
     * @param title is the title of the bug.
     * @param description is a complete description of the bug.
     * @param locale is the language in which this description has been written.
     * @param errorLevel is the estimated level of the bug. see {@link Level}.
     */
    Bug(final Member member, final Batch batch, final String title, final String description, final Locale locale, final Level errorLevel) {
        super(DaoBug.createAndPersist(member.getDao(), batch.getDao(), title, description, locale, errorLevel));
    }

    /**
     * Adds the comment.
     *
     * @param comment the comment
     * @see com.bloatit.data.DaoBug#addComment(com.bloatit.data.DaoComment)
     */
    public void addComment(final DaoComment comment) {
        getDao().addComment(comment);
    }

    /**
     * Sets the error level.
     *
     * @param level the new error level
     * @see com.bloatit.data.DaoBug#setErrorLevel(com.bloatit.data.DaoBug.Level)
     */
    public void setErrorLevel(final Level level) {
        getDao().setErrorLevel(level);
    }

    /**
     * Gets the member assigned to this bug. It is always the offer author.
     *
     * @return the assigned to
     * @see com.bloatit.data.DaoBug#getAssignedTo()
     */
    public Member getAssignedTo() {
        return Member.create(getDao().getAssignedTo());
    }

    /**
     * Gets this bug description.
     *
     * @return the description
     * @see com.bloatit.data.DaoBug#getDescription()
     */
    public final String getDescription() {
        return getDao().getDescription();
    }

    /**
     * Gets the locale in which this bug is written.
     *
     * @return the locale
     * @see com.bloatit.data.DaoBug#getLocale()
     */
    public final Locale getLocale() {
        return getDao().getLocale();
    }

    /**
     * Gets the error level.
     *
     * @return the error level
     * @see com.bloatit.data.DaoBug#getErrorLevel()
     */
    public final Level getErrorLevel() {
        return getDao().getErrorLevel();
    }

    /**
     * Gets the batch on which this bug has been added.
     *
     * @return the batch
     * @see com.bloatit.data.DaoBug#getBatch()
     */
    public Batch getBatch() {
        return Batch.create(getDao().getBatch());
    }

    /**
     * Gets the state.
     *
     * @return the state
     * @see com.bloatit.data.DaoBug#getState()
     */
    public State getState() {
        return getDao().getState();
    }

    /**
     * Sets the bug to resolved.
     *
     * @see com.bloatit.data.DaoBug#setResolved()
     */
    public void setResolved() {
        getDao().setResolved();
    }

    /**
     * Sets the bug to developing.
     *
     * @see com.bloatit.data.DaoBug#setDeveloping()
     */
    public void setDeveloping() {
        getDao().setDeveloping();
    }

    /**
     * Gets the comments on that bug.
     *
     * @return the comments
     * @see com.bloatit.data.DaoBug#getComments()
     */
    public final PageIterable<Comment> getComments() {
        return new CommentList(getDao().getComments());
    }

    /**
     * Gets the last update date.
     *
     * @return the last update date
     */
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
    public String getTitle() {
        return getDao().getTitle();
    }
}
