package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.BugList;
import com.bloatit.model.data.DaoBatch;
import com.bloatit.model.data.DaoBug;
import com.bloatit.model.data.DaoBug.Level;
import com.bloatit.model.data.DaoBug.State;

public class Batch extends Identifiable {

    private final DaoBatch dao;

    public static Batch create(final DaoBatch dao) {
        if (dao != null) {
            return new Batch(dao);
        }
        return null;
    }

    private Batch(final DaoBatch dao) {
        this.dao = dao;
    }

    @Override
    public int getId() {
        return getDao().getId();
    }

    public Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    public BigDecimal getAmount() {
        return getDao().getAmount();
    }

    public String getTitle() {
        return getDao().getDescription().getDefaultTranslation().getTitle();
    }

    public String getDescription() {
        return getDao().getDescription().getDefaultTranslation().getText();
    }

    public void addBug(Member member, Batch batch, String description, Locale locale, Level errorLevel){
        dao.addBug(new Bug(member, batch, description, locale, errorLevel).getDao());
    }

    /**
     * @param fatalPercent
     * @param majorPercent
     * @see com.bloatit.model.data.DaoBatch#updateMajorFatalPercent(int, int)
     */
    public void updateMajorFatalPercent(int fatalPercent, int majorPercent) {
        dao.updateMajorFatalPercent(fatalPercent, majorPercent);
    }

    /**
     * @see com.bloatit.model.data.DaoBatch#release()
     */
    public void release() {
        dao.release();
    }

    /**
     * @see com.bloatit.model.data.DaoBatch#validate()
     */
    public void validate() {
        dao.validate();
    }

    /**
     * @param level
     * @return
     * @see com.bloatit.model.data.DaoBatch#canValidatePart(com.bloatit.model.data.DaoBug.Level)
     */
    public boolean canValidatePart(Level level) {
        return dao.canValidatePart(level);
    }

    /**
     * @param level
     * @return
     * @see com.bloatit.model.data.DaoBatch#getNonResolvedBugs(com.bloatit.model.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getNonResolvedBugs(Level level) {
        return dao.getNonResolvedBugs(level);
    }

    /**
     * @param level
     * @return
     * @see com.bloatit.model.data.DaoBatch#getBugs(com.bloatit.model.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getBugs(Level level) {
        return dao.getBugs(level);
    }

    /**
     * @param state
     * @return
     * @see com.bloatit.model.data.DaoBatch#getBugs(com.bloatit.model.data.DaoBug.State)
     */
    public PageIterable<DaoBug> getBugs(State state) {
        return dao.getBugs(state);
    }

    /**
     * @param level
     * @param state
     * @return
     * @see com.bloatit.model.data.DaoBatch#getBugs(com.bloatit.model.data.DaoBug.Level,
     *      com.bloatit.model.data.DaoBug.State)
     */
    public PageIterable<Bug> getBugs(Level level, State state) {
        return new BugList(dao.getBugs(level, state));
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBatch#getReleaseDate()
     */
    public final Date getReleaseDate() {
        return dao.getReleaseDate();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBatch#getFatalBugsPercent()
     */
    public final int getFatalBugsPercent() {
        return dao.getFatalBugsPercent();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBatch#getMajorBugsPercent()
     */
    public final int getMajorBugsPercent() {
        return dao.getMajorBugsPercent();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoBatch#getMinorBugsPercent()
     */
    public final int getMinorBugsPercent() {
        return dao.getMinorBugsPercent();
    }

    DaoBatch getDao() {
        return dao;
    }

}
