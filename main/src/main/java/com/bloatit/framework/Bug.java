package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.model.data.DaoBug;
import com.bloatit.model.data.DaoBug.Level;
import com.bloatit.model.data.DaoBug.State;
import com.bloatit.model.data.DaoComment;

public class Bug extends Identifiable {

    private final DaoBug dao;

    public static Bug create(DaoBug dao) {
        if (dao != null) {
            return new Bug(dao);
        }
        return null;
    }

    private Bug(DaoBug dao) {
        super();
        this.dao = dao;
    }

    Bug(Member member, Batch batch, String description, Locale locale, Level errorLevel) {
        dao = new DaoBug(member.getDao(), batch.getDao(), description, locale, errorLevel);
    }

    @Override
    public int getId() {
        return dao.getId();
    }

    /**
     * @param comment
     * @see com.bloatit.model.data.DaoBug#addComment(com.bloatit.model.data.DaoComment)
     */
    public void addComment(DaoComment comment) {
        dao.addComment(comment);
    }

    /**
     * @param level
     * @see com.bloatit.model.data.DaoBug#setErrorLevel(com.bloatit.model.data.DaoBug.Level)
     */
    public void setErrorLevel(Level level) {
        dao.setErrorLevel(level);
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getAssignedTo()
     */
    public Member getAssignedTo() {
        return Member.create(dao.getAssignedTo());
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getDescription()
     */
    public final String getDescription() {
        return dao.getDescription();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getLocale()
     */
    public final Locale getLocale() {
        return dao.getLocale();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getErrorLevel()
     */
    public final Level getErrorLevel() {
        return dao.getErrorLevel();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getBatch()
     */
    public Batch getBatch() {
        return Batch.create(dao.getBatch());
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getState()
     */
    public State getState() {
        return dao.getState();
    }

    /**
     * @see com.bloatit.model.data.DaoBug#setResolved()
     */
    public void setResolved() {
        dao.setResolved();
    }

    /**
     * @see com.bloatit.model.data.DaoBug#setDeveloping()
     */
    public void setDeveloping() {
        dao.setDeveloping();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBug#getComments()
     */
    public final PageIterable<Comment> getComments() {
        return new CommentList(dao.getComments());
    }

    DaoBug getDao() {
        return dao;
    }

}
