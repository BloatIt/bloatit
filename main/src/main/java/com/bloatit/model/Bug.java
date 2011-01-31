package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoBug.State;
import com.bloatit.data.DaoComment;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.CommentList;

public class Bug extends Identifiable {

    private final DaoBug dao;

    public static Bug create(final DaoBug dao) {
        if (dao != null) {
            return new Bug(dao);
        }
        return null;
    }

    private Bug(final DaoBug dao) {
        super();
        this.dao = dao;
    }

    Bug(final Member member, final Batch batch, final String description, final Locale locale, final Level errorLevel) {
        dao = new DaoBug(member.getDao(), batch.getDao(), description, locale, errorLevel);
    }

    @Override
    public int getId() {
        return dao.getId();
    }

    /**
     * @param comment
     * @see com.bloatit.data.DaoBug#addComment(com.bloatit.data.DaoComment)
     */
    public void addComment(final DaoComment comment) {
        dao.addComment(comment);
    }

    /**
     * @param level
     * @see com.bloatit.data.DaoBug#setErrorLevel(com.bloatit.data.DaoBug.Level)
     */
    public void setErrorLevel(final Level level) {
        dao.setErrorLevel(level);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getAssignedTo()
     */
    public Member getAssignedTo() {
        return Member.create(dao.getAssignedTo());
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getDescription()
     */
    public final String getDescription() {
        return dao.getDescription();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getLocale()
     */
    public final Locale getLocale() {
        return dao.getLocale();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getErrorLevel()
     */
    public final Level getErrorLevel() {
        return dao.getErrorLevel();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getBatch()
     */
    public Batch getBatch() {
        return Batch.create(dao.getBatch());
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getState()
     */
    public State getState() {
        return dao.getState();
    }

    /**
     * @see com.bloatit.data.DaoBug#setResolved()
     */
    public void setResolved() {
        dao.setResolved();
    }

    /**
     * @see com.bloatit.data.DaoBug#setDeveloping()
     */
    public void setDeveloping() {
        dao.setDeveloping();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBug#getComments()
     */
    public final PageIterable<Comment> getComments() {
        return new CommentList(dao.getComments());
    }

    DaoBug getDao() {
        return dao;
    }

}
