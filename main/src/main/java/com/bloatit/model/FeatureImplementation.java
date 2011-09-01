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
import java.util.Locale;

import com.bloatit.common.Log;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.WrongStateException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.feature.AbstractFeatureState;
import com.bloatit.model.feature.DevelopingState;
import com.bloatit.model.feature.DiscardedState;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.feature.FinishedState;
import com.bloatit.model.feature.PendingState;
import com.bloatit.model.feature.PreparingState;
import com.bloatit.model.feature.TaskDevelopmentTimeOut;
import com.bloatit.model.feature.TaskUpdateDevelopingState;
import com.bloatit.model.lists.BugList;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.managers.HighlightFeatureManager;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.RgtFeature;
import com.bloatit.model.right.RgtOffer;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;

public final class FeatureImplementation extends Kudosable<DaoFeature> implements Feature {

    /** The state object. */
    private AbstractFeatureState stateObject;

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFeature, FeatureImplementation> {
        @SuppressWarnings("synthetic-access")
        @Override
        public FeatureImplementation doCreate(final DaoFeature dao) {
            return new FeatureImplementation(dao);
        }
    }

    /**
     * Create a new FeatureImplementation. This method is not protected by any
     * right management.
     * 
     * @param dao the dao
     * @return null if the <code>dao</code> is null.
     */
    @SuppressWarnings("synthetic-access")
    public static FeatureImplementation create(final DaoFeature dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Create a new feature. The right management for creating a feature is
     * specific. (The Right management system is not working in this case). You
     * have to use the {@link FeatureManager}.
     * 
     * @param author the author
     * @param locale the locale in which this feature is written
     * @param title the title of the feature
     * @param description the description of the feature
     * @param software the software {@link FeatureManager#canCreate(AuthToken)}
     *            to make sure you can create a new feature.
     * @see DaoFeature
     */
    public FeatureImplementation(final Member author,
                                 final Team team,
                                 final Locale locale,
                                 final String title,
                                 final String description,
                                 final Software software) {
        this(DaoFeature.createAndPersist(author.getDao(),
                                         DaoGetter.get(team),
                                         DaoDescription.createAndPersist(author.getDao(), DaoGetter.get(team), locale, title, description),
                                         DaoGetter.get(software)));
    }

    /**
     * Use the {@link #create(DaoFeature)} method.
     * 
     * @param dao the dao
     */
    private FeatureImplementation(final DaoFeature dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Can something
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canAccessComment(final Action action) {
        return canAccess(new RgtFeature.Comment(), action);
    }

    @Override
    public boolean canAccessContribution(final Action action) {
        return canAccess(new RgtFeature.Contribute(), action);
    }

    @Override
    public boolean canAccessOffer(final Action action) {
        return canAccess(new RgtFeature.Offer(), action);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Do things.
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Contribution addContribution(final BigDecimal amount, final String comment) throws NotEnoughMoneyException, UnauthorizedOperationException {
        tryAccess(new RgtFeature.Contribute(), Action.WRITE);
        // For exception safety keep the order.
        if (AuthToken.getAsTeam() != null) {
            if (AuthToken.getAsTeam().getUserTeamRight(AuthToken.getMember()).contains(UserTeamRight.BANK)) {
                Log.model().trace("Doing a contribution in the name of a team: " + AuthToken.getAsTeam().getId());
            } else {
                throw new UnauthorizedOperationException(SpecialCode.TEAM_CONTRIBUTION_WITHOUT_BANK);
            }
        }
        final DaoContribution contribution = getDao().addContribution(AuthToken.getMember().getDao(),
                                                                      DaoGetter.get(AuthToken.getAsTeam()),
                                                                      amount,
                                                                      comment);
        setStateObject(getStateObject().eventAddContribution());
        return Contribution.create(contribution);
    }

    @Override
    public Offer addOffer(final BigDecimal amount,
                          final String description,
                          final String license,
                          final Locale local,
                          final Date dateExpire,
                          final int secondsBeforeValidation) throws UnauthorizedOperationException {
        tryAccess(new RgtFeature.Offer(), Action.WRITE);
        final Offer offer = new Offer(AuthToken.getMember(),
                                      AuthToken.getAsTeam(),
                                      this,
                                      amount,
                                      description,
                                      license,
                                      local,
                                      dateExpire,
                                      secondsBeforeValidation);
        return doAddOffer(offer);
    }

    private Offer doAddOffer(final Offer offer) throws UnauthorizedOperationException {
        if (!offer.getFeature().equals(this)) {
            throw new IllegalArgumentException();
        }
        tryAccess(new RgtFeature.Offer(), Action.WRITE);

        // Warning: This does not works when the offer is created by a team
        if (!offer.getMember().equals(AuthToken.getMember())) {
            throw new UnauthorizedOperationException(SpecialCode.CREATOR_INSERTOR_MISMATCH);
        }
        getDao().addOffer(offer.getDao());
        setStateObject(getStateObject().eventAddOffer());
        return offer;
    }

    @Override
    public void removeOffer(final Offer offer) throws UnauthorizedOperationException {
        tryAccess(new RgtFeature.Offer(), Action.DELETE);
        if (getDao().getSelectedOffer().getId() != null && getDao().getSelectedOffer().getId().equals(offer.getId())) {
            setSelectedOffer(Offer.create(getDao().computeSelectedOffer()));
        }
        getDao().removeOffer(offer.getDao());
        setStateObject(getStateObject().eventRemoveOffer(offer));
    }

    @Override
    public Comment addComment(final String text) throws UnauthorizedOperationException {
        tryAccess(new RgtFeature.Comment(), Action.WRITE);
        final DaoComment comment = DaoComment.createAndPersist(this.getDao(),
                                                               DaoGetter.get(AuthToken.getAsTeam()),
                                                               AuthToken.getMember().getDao(),
                                                               text);
        getDao().addComment(comment);
        return Comment.create(comment);
    }

    /**
     * Used by Offer class. You should never have to use it
     * 
     * @param offer the offer to unselect. Nothing is done if the offer is not
     *            selected.
     */
    public void unSelectOffer(final Offer offer) {
        if (offer.equals(getSelectedOffer())) {
            setSelectedOffer(null);
            setSelectedOffer(Offer.create(getDao().computeSelectedOffer()));
        }
    }

    public boolean validateCurrentMilestone(final boolean force) {
        throwWrongStateExceptionOnNondevelopingState();
        return getSelectedOffer().validateCurrentMilestone(force);
    }

    @Override
    public void cancelDevelopment() throws UnauthorizedOperationException {
        getSelectedOffer().tryAccess(new RgtOffer.SelectedOffer(), Action.WRITE);
        setStateObject(getStateObject().eventDeveloperCanceled());
        // Work is done in the slot system.
    }

    @Override
    public void computeSelectedOffer() throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        setSelectedOffer(Offer.create(getDao().computeSelectedOffer()));
    }

    @Override
    public void setFeatureState(final FeatureState featureState) throws UnauthorizedOperationException {
        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        setFeatureStateUnprotected(featureState);
    }

    public void setFeatureStateUnprotected(final FeatureState featureState) {
        if (getFeatureState() != featureState) {
            switch (featureState) {
                case PENDING:
                    inPendingState();
                    break;
                case PREPARING:
                    inPreparingState();
                    break;
                case DEVELOPPING:
                    inDevelopmentState();
                    break;
                case DISCARDED:
                    inDiscardedState();
                    break;
                case FINISHED:
                    inFinishedState();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void delete() throws UnauthorizedOperationException {
        if (isDeleted()) {
            return;
        }

        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }

        this.setFeatureState(FeatureState.DISCARDED);

        // We delete every components of the feature (comments, offers,
        // translations ...)

        // We don't delete contributions because they are cancelled when going
        // into DISCARDED state. Not deleting them mean they will still appear
        // on the web site (for example on the account page) which is the
        // behavior we want.

        super.delete();

        for (final Comment comment : getComments()) {
            comment.delete();
        }

        for (final Offer offer : getOffers()) {
            offer.delete();
        }

        for (final Translation translation : getDescription().getTranslations()) {
            translation.delete();
        }

        for (final HighlightFeature hlFeature : HighlightFeatureManager.getAll()) {
            if (hlFeature.getFeature().getId().equals(getId())) {
                hlFeature.getDao().delete();
            }
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    // Slots and notification system
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Tells that we are in development state.
     */
    private void inDevelopmentState() {
        if (getDao().getSelectedOffer() == null || getDao().getSelectedOffer().getAmount().compareTo(getDao().getContribution()) > 0) {
            throw new WrongStateException("Cannot be in development state, not enough money.");
        }
        if (getSelectedOffer().isFinished()) {
            throw new BadProgrammerException("Cannot be in development state and have no milestone left.");
        }
        getDao().setFeatureState(FeatureState.DEVELOPPING);
        getSelectedOffer().getCurrentMilestone().setDevelopingUnprotected();
        new TaskDevelopmentTimeOut(getId(), getDao().getSelectedOffer().getCurrentMilestone().getExpirationDate());
    }

    /**
     * Slot called when the feature change to {@link DiscardedState}.
     */
    private void inDiscardedState() {
        getDao().setFeatureState(FeatureState.DISCARDED);

        for (final Contribution contribution : getContributionsUnprotected()) {
            contribution.cancel();
        }
        getSelectedOffer().cancelEverythingLeft();
    }

    /**
     * Slot called when this feature state change to {@link FinishedState}.
     */
    private void inFinishedState() {
        if (getDao().getSelectedOffer() == null || getDao().getSelectedOffer().hasMilestonesLeft()) {
            throw new WrongStateException("Cannot be in finished state if the current offer has milestone to validate.");
        }
        getDao().setFeatureState(FeatureState.FINISHED);
    }

    /**
     * Slot called when this feature state change to {@link PendingState}.
     */
    private void inPendingState() {
        if (getFeatureState() == FeatureState.PENDING) {
            return;
        }
        getDao().setFeatureState(FeatureState.PENDING);
    }

    /**
     * Slot called when this feature state change to {@link PreparingState}.
     */
    private void inPreparingState() {
        final PageIterable<DaoOffer> offers = getDao().getOffers();
        if (offers.size() < 1) {
            throw new WrongStateException("There must be at least one offer to be in Preparing state.");
        }
        if (offers.size() == 1) {
            setSelectedOffer(Offer.create(offers.iterator().next()));
        } else {
            setSelectedOffer(Offer.create(getDao().computeSelectedOffer()));
        }
        getDao().setFeatureState(FeatureState.PREPARING);
    }

    /**
     * Called by a {@link PlannedTask}. For now do nothing... A development
     * TimeOut is called when the expiration date arrive
     */
    public void developmentTimeOut() {
        setStateObject(getStateObject().eventDevelopmentTimeOut());
    }

    /**
     * Make sure this method is not accessible by any user (except admin).
     * <p>
     * Test if the current feature should passe into {@link DevelopingState}. Do
     * it if possible.
     * </p>
     * <p>
     * Called by a {@link PlannedTask}.
     * </p>
     */
    @Override
    public void updateDevelopmentState() {
        setStateObject(getStateObject().eventSelectedOfferTimeOut(getDao().getContribution()));
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Kudosable#notifyValid()
     */
    @Override
    protected void notifyValid() {
        if (getStateObject().getState() == FeatureState.DISCARDED) {
            setStateObject(getStateObject().eventPopularityPending());
        }
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Kudosable#notifyPending()
     */
    @Override
    protected void notifyPending() {
        if (getStateObject().getState() == FeatureState.DISCARDED) {
            setStateObject(getStateObject().eventPopularityPending());
        }
    }

    @Override
    protected void notifyRejected() {
        setStateObject(getStateObject().eventFeatureRejected());
    }

    /**
     * Sets the selected offer. Called internally and in featureState.
     * 
     * @param offer the new selected offer
     */
    private void setSelectedOffer(final Offer offer) {
        if (offer != null) {
            final Date validationDate = DateUtils.tomorrow();
            new TaskUpdateDevelopingState(getId(), validationDate);
            getDao().setValidationDate(validationDate);
            getDao().setSelectedOffer(offer.getDao());
        } else {
            getDao().setValidationDate(null);
            getDao().setSelectedOffer(null);
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Offer feedBack
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method called by Offer when the offer is kudosed. Update the
     * selectedOffer using it popularity.
     * 
     * @param offer the offer that has been kudosed.
     * @param positif true means kudos up, false kudos down.
     */
    public void notifyOfferKudos(final Offer offer, final boolean positif) {
        if (getFeatureState() == FeatureState.PREPARING) {
            computeSelectedOffer(offer, positif);
        }
    }

    /**
     * Update the selected offer using the popularity.
     * 
     * @param offer The offer that has been kudosed
     * @param positif true if this is a kudos, false if it is a unkudos.
     */
    private void computeSelectedOffer(final Offer offer, final boolean positif) {
        final Offer selectedOffer = getSelectedOffer();
        final boolean isSelectedOffer = offer.equals(selectedOffer);
        if (positif && !isSelectedOffer) {
            if (selectedOffer == null || offer.getPopularity() > selectedOffer.getPopularity()) {
                setSelectedOffer(Offer.create(offer.getDao()));
            }
        }
        if (!positif && isSelectedOffer) {
            if (selectedOffer == null || selectedOffer.getPopularity() < 0) {
                setSelectedOffer(Offer.create(getDao().computeSelectedOffer()));
            } else {
                for (final Offer thisOffer : getOffersUnprotected()) {
                    if (thisOffer.getPopularity() > selectedOffer.getPopularity()) {
                        setSelectedOffer(Offer.create(getDao().computeSelectedOffer()));
                        break;
                    }
                }
            }
        }

        if (getSelectedOffer() == null) {
            setFeatureStateUnprotected(FeatureState.PENDING);
        }
    }

    /**
     * Tell that the current selected offer is validate. This method is called
     * by {@link Offer} when needed.
     */
    public void setOfferIsValidated() {
        setStateObject(getStateObject().eventOfferIsValidated());
    }

    /**
     * Tell that the current milestone is validate. This method is called by
     * {@link Offer} when needed.
     */
    public void setMilestoneIsValidated() {
        setStateObject(getStateObject().eventMilestoneIsValidated());
    }

    /**
     * Tell that the current milestone is rejected. This method is called by
     * {@link Offer} when needed.
     */
    public void setMilestoneIsRejected() {
        setStateObject(getStateObject().eventMilestoneIsRejected());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Get something
    // /////////////////////////////////////////////////////////////////////////////////////////

    public boolean isDeveloping() {
        final boolean isDeveloping = getFeatureState() == FeatureState.DEVELOPPING;
        if (isDeveloping) {
            assert getSelectedOffer() != null;
            assert getValidatedOffer() != null;
            assert getSelectedOffer().equals(getValidatedOffer());
            assert getValidatedOffer().isFinished() == false;
        }
        return isDeveloping;
    }

    private void throwWrongStateExceptionOnNondevelopingState() {
        if (!isDeveloping()) {
            throw new WrongStateException("Feature should be in Developing state.");
        }
    }

    @Override
    public Date getValidationDate() {
        return getDao().getValidationDate();
    }

    @Override
    public Long getCommentsCount() {
        return getDao().getCommentsCount();
    }

    @Override
    public PageIterable<Comment> getComments() {
        return new CommentList(getDao().getComments());
    }

    /**
     * @see Feature#getContributions(boolean)
     */
    @Deprecated
    @Override
    public PageIterable<Contribution> getContributions() {
        return getContributionsUnprotected();
    }

    /**
     * Gets the contributions unprotected.
     * 
     * @return the contributions unprotected
     * @see #getContribution()
     */
    private PageIterable<Contribution> getContributionsUnprotected() {
        return new ContributionList(getDao().getContributions());
    }

    @Override
    public PageIterable<Contribution> getContributions(boolean isCanceled) {
        return new ContributionList(getDao().getContributions(isCanceled));
    }

    /** The Constant PROGRESSION_PERCENT. */
    public static final int PROGRESSION_PERCENT = 100;

    @Override
    public float getProgression() {
        return getDao().getProgress();
    }

    @Override
    public float getMemberProgression(final Member author) throws UnauthorizedOperationException {
        final BigDecimal memberAmount = getContributionOf(author);
        final float memberAmountFloat = memberAmount.floatValue();
        final float totalAmountFloat = getContribution().floatValue();
        final float progression = getProgression();
        return progression * memberAmountFloat / totalAmountFloat;
    }

    @Override
    public float getRelativeProgression(final BigDecimal amount) {
        final float memberAmountFloat = amount.floatValue();
        final float totalAmountFloat = getContribution().floatValue();
        final float progression = getProgression();

        return progression * memberAmountFloat / totalAmountFloat;
    }

    @Override
    public BigDecimal getContribution() {
        return getDao().getContribution();
    }

    @Override
    public BigDecimal getContributionMax() {
        BigDecimal contributionMax = getDao().getContributionMax();
        if (contributionMax == null) {
            return BigDecimal.ZERO;
        } else {
            return contributionMax;
        }
    }

    @Override
    public BigDecimal getContributionMin() {
        BigDecimal contributionMin = getDao().getContributionMin();
        if (contributionMin == null) {
            return BigDecimal.ZERO;
        } else {
            return contributionMin;
        }
    }

    @Override
    public BigDecimal getContributionOf(final Member member) {
        return getDao().getContributionOf(member.getDao());
    }

    @Override
    public Description getDescription() {
        return Description.create(getDao().getDescription());
    }

    @Override
    public Software getSoftware() {
        return Software.create(getDao().getSoftware());
    }

    @Override
    public boolean hasSoftware() {
        return getSoftware() != null;
    }

    @Override
    public PageIterable<Offer> getOffers() {
        return getOffersUnprotected();
    }

    /**
     * Gets the offers unprotected.
     * 
     * @return the offers unprotected
     */
    private PageIterable<Offer> getOffersUnprotected() {
        return new OfferList(getDao().getOffers());
    }

    @Override
    public Offer getSelectedOffer() {
        return Offer.create(getDao().getSelectedOffer());
    }

    @Override
    public Offer getValidatedOffer() {
        if (getDao().getSelectedOffer() != null && getValidationDate().before(new Date())) {
            return getSelectedOffer();
        }
        return null;
    }

    @Override
    public String getTitle() {
        return getDescription().getDefaultTranslation().getTitle();
    }

    @Override
    public FeatureState getFeatureState() {
        return getDao().getFeatureState();
    }

    /**
     * Sets the state object.
     * 
     * @param stateObject the new state object
     */
    private void setStateObject(final AbstractFeatureState stateObject) {
        this.stateObject = stateObject;
    }

    /**
     * Gets the state object.
     * 
     * @return the state object
     */
    private AbstractFeatureState getStateObject() {

        switch (getDao().getFeatureState()) {
            case PENDING:
                if (stateObject == null || !stateObject.getClass().equals(PendingState.class)) {
                    setStateObject(new PendingState(this));
                }
                break;
            case DEVELOPPING:
                if (stateObject == null || !stateObject.getClass().equals(DevelopingState.class)) {
                    setStateObject(new DevelopingState(this));
                }
                break;
            case DISCARDED:
                if (stateObject == null || !stateObject.getClass().equals(DiscardedState.class)) {
                    setStateObject(new DiscardedState(this));
                }
                break;
            case FINISHED:
                if (stateObject == null || !stateObject.getClass().equals(FinishedState.class)) {
                    setStateObject(new FinishedState(this));
                }
                break;
            case PREPARING:
                if (stateObject == null || !stateObject.getClass().equals(PreparingState.class)) {
                    setStateObject(new PreparingState(this));
                }
                break;
            default:
                assert false;
                break;
        }
        return stateObject;
    }

    // ////////////////////////////////////////////////////////////////////////
    // Kudosable configuration
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Turn pending.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return ModelConfiguration.getKudosableFeatureTurnPending();
    }

    /**
     * Turn valid.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return ModelConfiguration.getKudosableFeatureTurnValid();
    }

    /**
     * Turn rejected.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return ModelConfiguration.getKudosableFeatureTurnRejected();
    }

    /**
     * Turn hidden.
     * 
     * @return the int
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return ModelConfiguration.getKudosableFeatureTurnHidden();
    }

    @Override
    public int countOpenBugs() {
        return getDao().countOpenBugs();
    }

    @Override
    public PageIterable<Bug> getOpenBugs() {
        return new BugList(getDao().getOpenBugs());
    }

    @Override
    public PageIterable<Bug> getClosedBugs() {
        return new BugList(getDao().getClosedBugs());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
