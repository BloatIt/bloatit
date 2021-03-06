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
import java.util.Iterator;
import java.util.Locale;

import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.data.DaoRelease;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.model.lists.BugList;
import com.bloatit.model.lists.ListBinder;
import com.bloatit.model.lists.MilestoneContributionAmountList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RgtMilestone;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.UnauthorizedPublicAccessException;
import com.bloatit.model.visitor.ModelClassVisitor;

/**
 * A milestone is a part of an offer. Simple offers are only composed of one
 * milestone.
 * 
 * @author Thomas Guyard
 */
public final class Milestone extends Identifiable<DaoMilestone> {

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
        @SuppressWarnings("synthetic-access")
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
    @SuppressWarnings("synthetic-access")
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
     * @param title the title of the bug
     * @param description the description
     * @param locale the locale in which it is written
     * @param errorLevel the error level of the bug
     * @return the bug added in this milestone.
     * @throws UnauthorizedOperationException
     */
    public Bug addBug(final String title, final String description, final Language language, final Level errorLevel)
            throws UnauthorizedOperationException {
        final Bug bug = new Bug(AuthToken.getMember(), AuthToken.getAsTeam(), this, title, description, language, errorLevel);
        getDao().addBug(bug.getDao());
        return bug;
    }

    // ////////////////////////////////////////////////////////////////////////
    // Work-flow
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Tells if the authenticated user can access the <i>Release</i> property.
     * 
     * @param action the type of access you want to do on the <i>Release</i>
     *            property.
     * @return true if you can access the <i>Release</i> property.
     */
    public final boolean canAccessRelease(final Action action) {
        return canAccess(new RgtMilestone.Release(), action);
    }

    /**
     * <p>
     * Add a release on the current milestone. A milestone can have multiple
     * release. Each release reset the releasedDate of this milestone.
     * </p>
     * <p>
     * To finish the dev state of this milestone you have to validate this
     * milestone (done by {@link Offer#validateCurrentMilestone(boolean)}).
     * </p>
     * 
     * @throws UnauthorizedOperationException
     */
    public Release addRelease(final String description, final String version, final Locale locale, final FileMetadata file)
            throws UnauthorizedOperationException {
        tryAccess(new RgtMilestone.Release(), Action.WRITE);
        final Release release = new Release(getOffer().getMember(), AuthToken.getAsTeam(), this, description, version, locale);
        if (file != null) {
            release.addFile(file);
        }
        getDao().addRelease(release.getDao());
        return release;

    }

    public final boolean canAddRelease() {
        return canAccess(new RgtMilestone.Release(), Action.WRITE);
    }

    public void setDeveloping() throws UnauthorizedPublicAccessException {
        tryAccess(new RgtMilestone.State(), Action.WRITE);
        setDevelopingUnprotected();
    }

    public void setDevelopingUnprotected() {
        getDao().setDeveloping();
    }

    public void cancelMilestone() throws UnauthorizedPublicAccessException {
        tryAccess(new RgtMilestone.State(), Action.WRITE);
        getDao().cancelMilestone();
    }

    /**
     * Validate the milestone after it has been released.
     * 
     * @return true, if successful
     * @throws UnauthorizedPublicAccessException
     * @see com.bloatit.data.DaoMilestone#validate(boolean)
     */
    public boolean validate() throws UnauthorizedPublicAccessException {
        tryAccess(new RgtMilestone.State(), Action.WRITE);
        if (getDao().validate(false)) {
            getOffer().notifyMilestoneIsValidated(this);
            return true;
        }
        return false;
    }

    /**
     * Force validate the milestone after it has been released even if there are
     * bugs left.
     * 
     * @return true, if successful
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoMilestone#validate(boolean)
     */
    public boolean forceValidate() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        if (getDao().validate(true)) {
            getOffer().notifyMilestoneIsValidated(this);
            return true;
        }
        return false;
    }

    /**
     * Tells if a specified level is validated (and the corresponding amount has
     * been given to the developer)
     * 
     * @param level the level
     * @return true, the <code>level</code> is validated.
     */
    public boolean partIsValidated(final Level level) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtMilestone.State(), Action.WRITE);
        return getDao().partIsValidated(level);
    }

    /**
     * Tells if an admin should validate this milestone part.
     * 
     * @param level the level corresponding to the part we want to validate.
     * @return true, if we should do it, false otherwise.
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoMilestone#shouldValidatePart(com.bloatit.data.DaoBug.Level)
     */
    public boolean shouldValidatePart(final Level level) throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        return getDao().shouldValidatePart(level);
    }

    // ////////////////////////////////////////////////////////////////////////
    // getters
    // ////////////////////////////////////////////////////////////////////////

    public BugList getBugs() {
        return new BugList(getDao().getBugs());
    }

    /**
     * Gets the non resolved bugs.
     * 
     * @param level the level
     * @return the non resolved bugs for the level <code>level</code>.
     * @see com.bloatit.data.DaoMilestone#getNonResolvedBugs(com.bloatit.data.DaoBug.Level)
     */
    public BugList getNonResolvedBugs(final Level level) {
        return new BugList(getDao().getNonResolvedBugs(level));
    }

    /**
     * Gets the bugs on a specific <code>level</code>.
     * 
     * @param level the level
     * @return the bugs that are at <code>level</code>.
     * @see com.bloatit.data.DaoMilestone#getBugs(com.bloatit.data.DaoBug.Level)
     */
    public BugList getBugs(final Level level) {
        return new BugList(getDao().getBugs(level));
    }

    /**
     * Gets the bugs on a specific state.
     * 
     * @param state the state
     * @return the bugs
     * @see com.bloatit.data.DaoMilestone#getBugs(com.bloatit.data.DaoBug.BugState)
     */
    public BugList getBugs(final BugState state) {
        return new BugList(getDao().getBugs(state));
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
    public BugList getBugs(final Level level, final BugState state) {
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

    public final Date getValidationDate() {
        return new Date(getSecondBeforeValidation() * 1000 + getReleaseDate().getTime());
    }

    public final boolean hasBlockingBug() {
        return (getFatalBugsPercent() * getNonResolvedBugs(Level.FATAL).size() + getMajorBugsPercent() * getNonResolvedBugs(Level.MAJOR).size() + getMinorBugsPercent()
                * getNonResolvedBugs(Level.MINOR).size()) > 0;
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

    public final int getSecondBeforeValidation() {
        return getDao().getSecondBeforeValidation();
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

    protected Description getDescriptionEntity() {
        return Description.create(getDao().getDescription());
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
        final Iterator<DaoMilestone> iterator = getDao().getOffer().getMilestones().iterator();

        final int order = 1;
        while (iterator.hasNext()) {
            final Integer id = iterator.next().getId();
            if (id != null && id.equals(getDao().getId())) {
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

    public BigDecimal getAmountPaid() {
        return getDao().getAmountPaid();
    }

    public Date getLastPaymentDate() {
        return getDao().getLastPaymentDate();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public PageIterable<MilestoneContributionAmount> getContributionAmounts() {
        return new MilestoneContributionAmountList(getDao().getContributionAmounts());
    }

}
