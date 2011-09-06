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
import java.util.Locale;

import com.bloatit.common.Log;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoOffer;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.MilestoneList;
import com.bloatit.model.managers.ContributionManager;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RgtOffer;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;
import com.bloatit.model.right.UnauthorizedPublicAccessException;

public final class Offer extends Kudosable<DaoOffer> {

    // ////////////////////////////////////////////////////////////////////////
    // Construction
    // ////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoOffer, Offer> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Offer doCreate(final DaoOffer dao) {
            return new Offer(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Offer create(final DaoOffer dao) {
        return new MyCreator().create(dao);
    }

    public Offer(final Member member,
                 final Team team,
                 final Feature feature,
                 final BigDecimal amount,
                 final String description,
                 final String license,
                 final Locale local,
                 final Date dateExpire,
                 final int secondsBeforeValidation) {
        super(new DaoOffer(member.getDao(),
                           DaoGetter.get(team),
                           ((FeatureImplementation) feature).getDao(),
                           amount,
                           DaoDescription.createAndPersist(member.getDao(), DaoGetter.get(team), local, "RFU", description),
                           license,
                           dateExpire,
                           secondsBeforeValidation));
    }

    private Offer(final DaoOffer dao) {
        super(dao);
    }

    public Milestone addMilestone(final BigDecimal amount,
                                  final String description,
                                  final Locale local,
                                  final Date dateExpire,
                                  final int secondBeforeValidation) throws UnauthorizedOperationException {
        if (!isDraft()) {
            throw new BadProgrammerException("You cannot add a milestone on a non draft offer.");
        }
        tryAccess(new RgtOffer.Milestone(), Action.WRITE);
        final DaoMilestone daoMilestone = new DaoMilestone(dateExpire,
                                                           amount,
                                                           DaoDescription.createAndPersist(getDao().getMember(),
                                                                                           DaoGetter.get(AuthToken.getAsTeam()),
                                                                                           local,
                                                                                           "RFU",
                                                                                           description), getDao(), secondBeforeValidation);
        getDao().addMilestone(daoMilestone);
        return Milestone.create(daoMilestone);
    }

    // ////////////////////////////////////////////////////////////////////////
    // Work-flow
    // ////////////////////////////////////////////////////////////////////////

    public void setDraftFinished() throws UnauthorizedPublicAccessException {
        tryAccess(new RgtOffer.Draft(), Action.WRITE);
        getDao().setDraft(false);
    }

    boolean validateCurrentMilestone(final boolean force) {
        // If the validation is not complete, there is nothing to do in the
        // feature
        final DaoMilestone currentMilestone = findCurrentDaoMilestone();
        if (currentMilestone == null) {
            Log.framework().error("You tried to validate a milestone, but no milestone to validate found.");
            return false;
        }
        final boolean isAllValidated = currentMilestone.validate(force);
        if (isAllValidated) {
            if (getDao().hasMilestonesLeft()) {
                getFeatureImplementation().setMilestoneIsValidated();
            } else {
                getFeatureImplementation().setOfferIsValidated();
            }
        }
        return isAllValidated;
    }

    protected void notifyMilestoneIsValidated(final Milestone milestone) {
        if (!milestone.getOffer().equals(this)) {
            throw new BadProgrammerException("This offer is not the owner of this milestone.");
        }
        if (getDao().hasMilestonesLeft()) {
            getFeatureImplementation().setMilestoneIsValidated();
        } else {
            getFeatureImplementation().setOfferIsValidated();
        }
    }

    // Must be internal call. Make me protected ?
    protected boolean shouldValidateCurrentMilestonePart(final Level level) {
        final DaoMilestone currentMilestone = findCurrentDaoMilestone();
        if (currentMilestone == null) {
            return false;
        }
        return currentMilestone.shouldValidatePart(level);
    }

    void cancelEverythingLeft() {
        getDao().cancelEverythingLeft();
    }

    private DaoMilestone findCurrentDaoMilestone() {
        if (getDao().hasMilestonesLeft()) {
            return getDao().getCurrentMilestone();
        }
        return null;
    }

    private boolean hasMilestoneLeft() {
        return findCurrentDaoMilestone() != null;
    }

    @Override
    public void delete() throws UnauthorizedOperationException {
        if (isDeleted()) {
            return;
        }

        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }

        // Delete all subcomponents of the offer.
        // Because milestones are not userContents, we delete all subcomponents
        // of milestones directly.
        for (final Milestone milestone : getMilestones()) {
            for (final Bug bug : milestone.getBugs()) {
                bug.delete();
            }

            for (final Translation translation : milestone.getDescriptionEntity().getTranslations()) {
                translation.delete();
            }

            for (final Release release : milestone.getReleases()) {
                release.delete();
            }
        }

        super.delete();
    }

    // ////////////////////////////////////////////////////////////////////////
    // Notifications
    // ////////////////////////////////////////////////////////////////////////

    @Override
    public EnumSet<SpecialCode> canVoteUp() {
        EnumSet<SpecialCode> set = super.canVoteUp();
        if (!canVote()) {
            set.add(SpecialCode.YOU_HAVE_TO_CONTRIBUTE_TO_VOTE_ON_OFFER);
        }
        return set;
    }

    @Override
    public EnumSet<SpecialCode> canVoteDown() {
        EnumSet<SpecialCode> set = super.canVoteDown();
        if (!canVote()) {
            set.add(SpecialCode.YOU_HAVE_TO_CONTRIBUTE_TO_VOTE_ON_OFFER);
        }
        return set;
    }

    private boolean canVote() {
        if (!AuthToken.isAuthenticated()) {
            return false;
        }
        if (ContributionManager.getByFeatureMember(getFeature(), AuthToken.getMember()).size() > 0) {
            return true;
        }
        if (getMember().equals(AuthToken.getMember())) {
            return true;
        }
        return false;
    }

    @Override
    protected void notifyKudos(final boolean positif) {
        getFeatureImplementation().notifyOfferKudos(this, positif);
    }

    @Override
    protected void notifyRejected() {
        getFeature().unSelectOffer(this);
    }

    // ////////////////////////////////////////////////////////////////////////
    // Getters
    // ////////////////////////////////////////////////////////////////////////

    // Public data, no right management.
    public boolean isFinished() {
        return !hasMilestoneLeft();
    }

    // Public data, no right management.
    public boolean isDraft() {
        return getDao().isDraft();
    }

    // Public data, no right management.
    public FeatureImplementation getFeature() {
        return getFeatureImplementation();
    }

    // Public data, no right management.
    private FeatureImplementation getFeatureImplementation() {
        return FeatureImplementation.create(getDao().getFeature());
    }

    // Public data, no right management.
    public BigDecimal getAmount() {
        return getDao().getAmount();
    }

    // Public data, no right management.
    public PageIterable<Milestone> getMilestones() {
        return new MilestoneList(getDao().getMilestones());
    }

    // Public data, no right management.
    public Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    /** The Constant PROGRESSION_PERCENT. */
    private static final int PROGRESSION_PERCENT = 100;

    /**
     * Return the progression of the funding of this offer with the amount
     * available on the feature
     */
    // Public data, no right management.
    public float getProgression() {
        if (getAmount().floatValue() != 0) {
            return (getFeature().getContribution().floatValue() * PROGRESSION_PERCENT) / getAmount().floatValue();
        }
        return Float.POSITIVE_INFINITY;
    }

    // Public data, no right management.
    public Milestone getCurrentMilestone() {
        return Milestone.create(getDao().getCurrentMilestone());
    }

    // Public data, no right management.
    public Milestone getLastMilestone() {
        return Milestone.create(getDao().getLastMilestone());
    }

    // Public data, no right management.
    public boolean hasRelease() {
        return getDao().hasRelease();
    }

    // Public data, no right management.
    public Release getLastRelease() {
        return Release.create(getDao().getLastRelease());
    }

    // Public data, no right management.
    public String getlicense() {
        return getDao().getLicense();
    }

    // ////////////////////////////////////////////////////////////////////////
    // Kudosable configuration
    // ////////////////////////////////////////////////////////////////////////

    /**
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return ModelConfiguration.getKudosableOfferTurnPending();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return ModelConfiguration.getKudosableOfferTurnValid();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return ModelConfiguration.getKudosableOfferTurnRejected();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return ModelConfiguration.getKudosableOfferTurnHidden();
    }

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
