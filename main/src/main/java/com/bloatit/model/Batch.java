package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoBug.State;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.BugList;

public class Batch extends Identifiable<DaoBatch> {

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    public static Batch create(final DaoBatch dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoBatch> created = CacheManager.get(dao);
            if (created == null) {
                return new Batch(dao);
            }
            return (Batch) created;
        }
        return null;
    }

    private Batch(final DaoBatch dao) {
        super(dao);
    }

    /**
     * @param fatalPercent
     * @param majorPercent
     * @see com.bloatit.data.DaoBatch#updateMajorFatalPercent(int, int)
     */
    public void updateMajorFatalPercent(final int fatalPercent, final int majorPercent) {
        getDao().updateMajorFatalPercent(fatalPercent, majorPercent);
    }

    public void addBug(final Member member, final Batch batch, final String description, final Locale locale, final Level errorLevel) {
        getDao().addBug(new Bug(member, batch, description, locale, errorLevel).getDao());
    }

    // ////////////////////////////////////////////////////////////////////////
    // Work-flow
    // ////////////////////////////////////////////////////////////////////////

    /**
     * @see com.bloatit.data.DaoBatch#release()
     */
    public void release() {
        getDao().release();
    }

    /**
     * @see com.bloatit.data.DaoBatch#validate()
     */
    public boolean validate() {
        return getDao().validate(false);
    }

    /**
     * @see com.bloatit.data.DaoBatch#validate()
     */
    public boolean forceValidate() {
        return getDao().validate(true);
    }

    /**
     * @param level
     * @return
     * @see com.bloatit.data.DaoBatch#shouldValidatePart(com.bloatit.data.DaoBug.Level)
     */
    public boolean shouldValidatePart(final Level level) {
        return getDao().shouldValidatePart(level);
    }

    // ////////////////////////////////////////////////////////////////////////
    // getters
    // ////////////////////////////////////////////////////////////////////////

    /**
     * @param level
     * @return
     * @see com.bloatit.data.DaoBatch#getNonResolvedBugs(com.bloatit.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getNonResolvedBugs(final Level level) {
        return getDao().getNonResolvedBugs(level);
    }

    /**
     * @param level
     * @return
     * @see com.bloatit.data.DaoBatch#getBugs(com.bloatit.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getBugs(final Level level) {
        return getDao().getBugs(level);
    }

    /**
     * @param state
     * @return
     * @see com.bloatit.data.DaoBatch#getBugs(com.bloatit.data.DaoBug.State)
     */
    public PageIterable<DaoBug> getBugs(final State state) {
        return getDao().getBugs(state);
    }

    /**
     * @param level
     * @param state
     * @return
     * @see com.bloatit.data.DaoBatch#getBugs(com.bloatit.data.DaoBug.Level,
     *      com.bloatit.data.DaoBug.State)
     */
    public PageIterable<Bug> getBugs(final Level level, final State state) {
        return new BugList(getDao().getBugs(level, state));
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBatch#getReleaseDate()
     */
    public final Date getReleaseDate() {
        return getDao().getReleaseDate();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBatch#getFatalBugsPercent()
     */
    public final int getFatalBugsPercent() {
        return getDao().getFatalBugsPercent();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBatch#getMajorBugsPercent()
     */
    public final int getMajorBugsPercent() {
        return getDao().getMajorBugsPercent();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoBatch#getMinorBugsPercent()
     */
    public final int getMinorBugsPercent() {
        return getDao().getMinorBugsPercent();
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
}
