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

import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoRelease;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.BugList;
import com.bloatit.model.lists.ListBinder;

/**
 * A milestone is a part of an offer. Simple offers are only composed of one milestone.
 *
 * @author Thomas Guyard
 */
public class Milestone extends Identifiable<DaoMilestone> {

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoMilestone, Milestone> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @Override
        public Milestone doCreate(final DaoMilestone dao) {
            return new Milestone(dao);
        }
    }

    /**
     * Check the cache, if a corresponding Milestone exist return it, otherwise
     * create a new one using its dao representation. If the dao == null return
     * null;
     *
     * @param dao the dao
     * @return the milestone or null if the dao == null
     * @see Creator
     */
    public static Milestone create(final DaoMilestone dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new milestone.
     *
     * @param dao the dao
     */
    private Milestone(final DaoMilestone dao) {
        super(dao);
    }

    /**
     * Update major fatal percent.
     *
     * @param fatalPercent the fatal percent
     * @param majorPercent the major percent
     * @see com.bloatit.data.DaoMilestone#updateMajorFatalPercent(int, int)
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
     * @return the bug added in this milestone.
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
     * Add a release on the current milestone. A milestone can have multiple release.
     * Each release reset the releasedDate of this milestone.
     * </p>
     * <p>
     * To finish the dev state of this milestone you have to validate this milestone
     * (done by {@link Offer#validateCurrentMilestone(boolean)}).
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

    public void cancelMilestone() {
        getDao().cancelMilestone();
    }

    /**
     * Validate the milestone after it has been relreased.
     *
     * @return true, if successful
     * @see com.bloatit.data.DaoMilestone#validate(boolean)
     */
    public boolean validate() {
        return getDao().validate(false);
    }

    /**
     * Force validate the milestone after it has been released even if there are
     * bugs left.
     *
     * @return true, if successful
     * @see com.bloatit.data.DaoMilestone#validate(boolean)
     */
    public boolean forceValidate() {
        return getDao().validate(true);
    }

    /**
     * Tells if an admin should validate this milestone part.
     *
     * @param level the level corresponding to the part we want to validate.
     * @return true, if we should do it, false otherwise.
     * @see com.bloatit.data.DaoMilestone#shouldValidatePart(com.bloatit.data.DaoBug.Level)
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
     * @see com.bloatit.data.DaoMilestone#getNonResolvedBugs(com.bloatit.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getNonResolvedBugs(final Level level) {
        return getDao().getNonResolvedBugs(level);
    }

    /**
     * Gets the bugs on a specific <code>level</code>.
     *
     * @param level the level
     * @return the bugs that are at <code>level</code>.
     * @see com.bloatit.data.DaoMilestone#getBugs(com.bloatit.data.DaoBug.Level)
     */
    public PageIterable<DaoBug> getBugs(final Level level) {
        return getDao().getBugs(level);
    }

    /**
     * Gets the bugs on a specific state.
     *
     * @param state the state
     * @return the bugs
     * @see com.bloatit.data.DaoMilestone#getBugs(com.bloatit.data.DaoBug.BugState)
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
     * @see com.bloatit.data.DaoMilestone#getBugs(com.bloatit.data.DaoBug.Level,
     *      com.bloatit.data.DaoBug.BugState)
     */
    public PageIterable<Bug> getBugs(final Level level, final BugState state) {
        return new BugList(getDao().getBugs(level, state));
    }

    /**
     * Gets the release date.
     *
     * @return the release date
     * @see com.bloatit.data.DaoMilestone#getReleasedDate()
     */
    public final Date getReleaseDate() {
        return getDao().getReleasedDate();
    }

    /**
     * Gets the fatal bugs percent.
     *
     * @return the fatal bugs percent
     * @see com.bloatit.data.DaoMilestone#getFatalBugsPercent()
     */
    public final int getFatalBugsPercent() {
        return getDao().getFatalBugsPercent();
    }

    /**
     * Gets the major bugs percent.
     *
     * @return the major bugs percent
     * @see com.bloatit.data.DaoMilestone#getMajorBugsPercent()
     */
    public final int getMajorBugsPercent() {
        return getDao().getMajorBugsPercent();
    }

    /**
     * Gets the minor bugs percent.
     *
     * @return the minor bugs percent
     * @see com.bloatit.data.DaoMilestone#getMinorBugsPercent()
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
        final Iterator<DaoMilestone> iterator = getDao().getOffer().getMilestonees().iterator();

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

    public MilestoneState getMilestoneState() {
        return getDao().getMilestoneState();
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
