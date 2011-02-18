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

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;

import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoBug.State;
import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.BugList;

/**
 * A batch is a part of an offer. Simple offers are only composed of one batch.
 *
 * @author Thomas Guyard
 *
 */
public class Batch extends Identifiable<DaoBatch> {

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Check the cache, if a corresponding Batch exist return it, otherwise create a new
     * one using its dao representation. If the dao == null return null;
     */
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

    public Bug addBug(final Member member, final String title, final String description, final Locale locale, final Level errorLevel) {
        Bug bug = new Bug(member, this, title, description, locale, errorLevel);
        getDao().addBug(bug.getDao());
        return bug;
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
     * @see com.bloatit.data.DaoBatch#validate(boolean)
     */
    public boolean validate() {
        return getDao().validate(false);
    }

    /**
     * @see com.bloatit.data.DaoBatch#validate(boolean)
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
     * com.bloatit.data.DaoBug.State)
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

    @Override
    protected boolean isMine(Member member) {
        return Offer.create(this.getDao().getOffer()).isMine(member);
    }

    @Override
    protected EnumSet<UserGroupRight> calculateMyGroupRights(Member member) {
        return Offer.create(this.getDao().getOffer()).calculateMyGroupRights(member);
    }
    
    public int getPosition() {
        Iterator<DaoBatch> iterator = getDao().getOffer().getBatches().iterator();

        int order = 1;
        while(iterator.hasNext()) {
            if(iterator.next().getId() == getDao().getId()) {
                return order;
            }
        }
        return -1;
    }
}
