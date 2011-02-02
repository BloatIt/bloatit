package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoBug.State;
import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;

/**
 * This is a bug report. A bug report is associated with a batch. it is quite similar to
 * the bug report in a classical bugTracker.
 * 
 * @author Thomas Guyard
 */
public class Bug extends Identifiable<DaoBug> {

    /**
     * Find a bug in the cache or create an new one.
     * 
     * @param dao
     * @return if dao is null return null. Else return the new Bug.
     */
    public static Bug create(final DaoBug dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoBug> created = CacheManager.get(dao);
            if (created == null) {
                return new Bug(dao);
            }
            return (Bug) created;
        }
        return null;
    }

    private Bug(final DaoBug dao) {
        super(dao);
    }

    /**
     * Create a new Bug.
     * @param member is the author of the bug.
     * @param batch is the batch on which this bug has been set.
     * @param description is a complete description of the bug.
     * @param locale is the language in which this description has been written.
     * @param errorLevel is the estimated level of the bug. see {@link Level}.
     */
    Bug(final Member member, final Batch batch, final String description, final Locale locale, final Level errorLevel) {
        super(new DaoBug(member.getDao(), batch.getDao(), description, locale, errorLevel));
    }

    /**
     * @param comment
     * @see com.bloatit.data.DaoBug#addComment(com.bloatit.data.DaoComment)
     */
    public void addComment(final DaoComment comment) {
        getDao().addComment(comment);
    }

    /**
     * @param level
     * @see com.bloatit.data.DaoBug#setErrorLevel(com.bloatit.data.DaoBug.Level)
     */
    public void setErrorLevel(final Level level) {
        getDao().setErrorLevel(level);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getAssignedTo()
     */
    public Member getAssignedTo() {
        return Member.create(getDao().getAssignedTo());
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getDescription()
     */
    public final String getDescription() {
        return getDao().getDescription();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getLocale()
     */
    public final Locale getLocale() {
        return getDao().getLocale();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getErrorLevel()
     */
    public final Level getErrorLevel() {
        return getDao().getErrorLevel();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getBatch()
     */
    public Batch getBatch() {
        return Batch.create(getDao().getBatch());
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getState()
     */
    public State getState() {
        return getDao().getState();
    }

    /**
     * @see com.bloatit.data.DaoBug#setResolved()
     */
    public void setResolved() {
        getDao().setResolved();
    }

    /**
     * @see com.bloatit.data.DaoBug#setDeveloping()
     */
    public void setDeveloping() {
        getDao().setDeveloping();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getComments()
     */
    public final PageIterable<Comment> getComments() {
        return new CommentList(getDao().getComments());
    }
}
