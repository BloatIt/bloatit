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
import com.bloatit.data.DaoBatch.BatchState;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoRelease;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.BugList;
import com.bloatit.model.lists.ListBinder;

/**
 * A batch is a part of an offer. Simple offers are only composed of one batch.
 *
 * @author Thomas Guyard
 */
public class Batch extends Identifiable<DaoBatch> {

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoBatch, Batch> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @Override
        public Batch doCreate(final DaoBatch dao) {
            return new Batch(dao);
        }
    }

    /**
     * Check the cache, if a corresponding Batch exist return it, otherwise
     * create a new one using its dao representation. If the dao == null return
     * null;
     *
     * @param dao the dao
     * @return the batch or null if the dao == null
     * @see Creator
     */
    public static Batch create(final DaoBatch dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new batch.
     *
     * @param dao the dao
     */
    private Batch(final DaoBatch dao) {
        super(dao);
    }

    /**
     * Update major fatal percent.
     *
     * @param fatalPercent the fatal percent
     * @param majorPercent the major percent
     * @see com.bloatit.data.DaoBatch#updateMajorFatalPercent(int, int)
     */
    public void updateMajorFatalPercent(final int fatalPercent, final int majorPercent) {
        getDao().updateMajorFatalPercent(fatalPercent, majorPercent);
    }

    /**
     * Adds the bug.
     *
     * @param member the author of the bug
     * @param title the title of the bug
     * @param description the description
     * @param locale the locale in which it is written
     * @param errorLevel the error level of the bug
     * @return the bug added in this batch.
     */
    public Bug addBug(final Member member, final String title, final String description, final Locale locale, final Level errorLevel) {
        final Bug bug = new Bug(member, this, title, description, locale, errorLevel);
        getDao().addBug(bug.getDao());
        return bug;
    }

    // ////////////////////////////////////////////////////////////////////////
    // Work-flow
    // ////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * Add a release on the current batch. A batch can have multiple release.
     * Each release reset the releasedDate of this batch.
     * </p>
     * <p>
     * To finish the dev state of this batch you have to validate this batch
     * (done by {@link Offer#validateCurrentBatch(boolean)}).
     * </p>
     */
    public void addRelease(final String description, final String version, final Locale locale, FileMetadata file) {
        Release release = new Release(getOffer().getAuthor(), this, description, version, locale);
        if (file != null) {
            release.addFile(file);
        }
        // TODO as team ?
        // if (getOffer().getAsTeam() != null){
        // release.setAsTeam(getOffer().getAsTeam());
        // }
        getDao().addRelease(release.getDao());

    }

    public void setDeveloping() {
        getDao().setDeveloping();
    }

    public void cancelBatch() {
        getDao().cancelBatch();
    }

    /**
     * Validate the batch after it has been relreased.
     *
     * @return true, if successful
     * @see com.bloatit.data.DaoBatch#validate(boolean)
     */
    public boolean validate() {
        return getDao().validate(false);
    }

    /**
     * Force validate the batch after it has been released even if there are
     * bugs left.
     *
     * @return true, if successful
     * @see com.bloatit.data.DaoBatch#validate(boolean)
     */
    public boolean forceValidate() {
        return getDao().validate(true);
    }

    /**
     * Tells if an admin should validate this batch part.
     *
     * @param level the level corresponding to the part we want to validate.
     * @return true, if we should do it, false otherwise.
     * @see com.bloatit.data.DaoBatch#shouldValidatePart(com.bloatit.data.DaoBug.Level)
     */
    public boolean shouldValidatePart(final Level level) {
        return getDao().shouldValidatePart(level);
    }

    // TODO doc
    public boolean partIsValidated(final Level level) {
        return getDao().partIsValidated(level);
    }

    // ////////////////////////////////////////////////////////////////////////
    // getters
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Gets the non resolved bugs.
     *
     * @param level the level
     * @return the non resolved bugs for the level <code>level</code>.
     * @see com.bloatit.data.DaoBatch#getNonResolvedBugs(com.bloatit.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getNonResolvedBugs(final Level level) {
        return getDao().getNonResolvedBugs(level);
    }

    /**
     * Gets the bugs on a specific <code>level</code>.
     *
     * @param level the level
     * @return the bugs that are at <code>level</code>.
     * @see com.bloatit.data.DaoBatch#getBugs(com.bloatit.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getBugs(final Level level) {
        return getDao().getBugs(level);
    }

    /**
     * Gets the bugs on a specific state.
     *
     * @param state the state
     * @return the bugs
     * @see com.bloatit.data.DaoBatch#getBugs(com.bloatit.data.DaoBug.BugState)
     */
    public PageIterable<DaoBug> getBugs(final BugState state) {
        return getDao().getBugs(state);
    }

    /**
     * Gets the bugs on a specific <code>level</code> and <code>state</code>.
     *
     * @param level the level
     * @param state the state
     * @return the bugs
     * @see com.bloatit.data.DaoBatch#getBugs(com.bloatit.data.DaoBug.Level,
     *      com.bloatit.data.DaoBug.BugState)
     */
    public PageIterable<Bug> getBugs(final Level level, final BugState state) {
        return new BugList(getDao().getBugs(level, state));
    }

    /**
     * Gets the release date.
     *
     * @return the release date
     * @see com.bloatit.data.DaoBatch#getReleasedDate()
     */
    public final Date getReleaseDate() {
        return getDao().getReleasedDate();
    }

    /**
     * Gets the fatal bugs percent.
     *
     * @return the fatal bugs percent
     * @see com.bloatit.data.DaoBatch#getFatalBugsPercent()
     */
    public final int getFatalBugsPercent() {
        return getDao().getFatalBugsPercent();
    }

    /**
     * Gets the major bugs percent.
     *
     * @return the major bugs percent
     * @see com.bloatit.data.DaoBatch#getMajorBugsPercent()
     */
    public final int getMajorBugsPercent() {
        return getDao().getMajorBugsPercent();
    }

    /**
     * Gets the minor bugs percent.
     *
     * @return the minor bugs percent
     * @see com.bloatit.data.DaoBatch#getMinorBugsPercent()
     */
    public final int getMinorBugsPercent() {
        return getDao().getMinorBugsPercent();
    }

    /**
     * Gets the expiration date.
     *
     * @return the expiration date
     */
    public Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    /**
     * Gets the amount.
     *
     * @return the amount
     */
    public BigDecimal getAmount() {
        return getDao().getAmount();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return getDao().getDescription().getDefaultTranslation().getTitle();
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return getDao().getDescription().getDefaultTranslation().getText();
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public int getPosition() {
        final Iterator<DaoBatch> iterator = getDao().getOffer().getBatches().iterator();

        final int order = 1;
        while (iterator.hasNext()) {
            if (iterator.next().getId() == getDao().getId()) {
                return order;
            }
        }
        return -1;
    }

    public Offer getOffer() {
        return Offer.create(getDao().getOffer());
    }

    public BatchState getBatchState() {
        return getDao().getBatchState();
    }

    public PageIterable<Release> getReleases() {
        return new ListBinder<Release, DaoRelease>(getDao().getReleases());
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedObject#isMine(com.bloatit.model.Member)
     */
    @Override
    protected boolean isMine(final Member member) {
        return Offer.create(getDao().getOffer()).isMine(member);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.model.right.RestrictedObject#calculateMyTeamRights(com.bloatit
     * .model .Member)
     */
    @Override
    protected EnumSet<UserTeamRight> calculateMyTeamRights(final Member member) {
        return Offer.create(getDao().getOffer()).calculateMyTeamRights(member);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
