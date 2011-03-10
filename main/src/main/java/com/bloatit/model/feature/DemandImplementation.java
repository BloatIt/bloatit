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
package com.bloatit.model.feature;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Creator;
import com.bloatit.model.Feature;
import com.bloatit.model.Description;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Member;
import com.bloatit.model.ModelClassVisitor;
import com.bloatit.model.ModelConfiguration;
import com.bloatit.model.Offer;
import com.bloatit.model.PlannedTask;
import com.bloatit.model.Project;
import com.bloatit.model.lists.BugList;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.DemandRight;

// TODO : delete comment.
//

/**
 * A demand is an idea :). It represent a demand made by one user.
 */
public final class DemandImplementation extends Kudosable<DaoFeature> implements Feature {

    /** The state object. */
    private AbstractDemandState stateObject;

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFeature, DemandImplementation> {
        @Override
        public DemandImplementation doCreate(final DaoFeature dao) {
            return new DemandImplementation(dao);
        }
    }

    /**
     * Create a new DemandImplementation. This method is not protected by any
     * right management.
     *
     * @param dao the dao
     * @return null if the <code>dao</code> is null.
     */
    public static DemandImplementation create(final DaoFeature dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Create a new demand. The right management for creating a demand is
     * specific. (The Right management system is not working in this case). You
     * have to use the {@link DemandManager}.
     *
     * @param author the author
     * @param locale the locale in which this demand is written
     * @param title the title of the demand
     * @param description the description of the demand
     * @param project the project {@link DemandManager#canCreate(AuthToken)} to
     *            make sure you can create a new demand.
     * @see DaoFeature
     */
    public DemandImplementation(final Member author, final Locale locale, final String title, final String description, final Project project) {
        this(DaoFeature.createAndPersist(author.getDao(),
                                        DaoDescription.createAndPersist(author.getDao(), locale, title, description),
                                        project.getDao()));
    }

    /**
     * Use the {@link #create(DaoFeature)} method.
     *
     * @param dao the dao
     */
    private DemandImplementation(final DaoFeature dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Can something
    // /////////////////////////////////////////////////////////////////////////////////////////

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#canAccessComment(com.bloatit .model.right
     * .RightManager.Action)
     */
    @Override
    public boolean canAccessComment(final Action action) {
        return canAccess(new DemandRight.Comment(), action);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#canAccessContribution(com.bloatit .model
     * .right.RightManager.Action)
     */
    @Override
    public boolean canAccessContribution(final Action action) {
        return canAccess(new DemandRight.Contribute(), action);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#canAccessOffer(com.bloatit.model .right
     * .RightManager.Action)
     */
    @Override
    public boolean canAccessOffer(final Action action) {
        return canAccess(new DemandRight.Offer(), action);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#canAccessDescription()
     */
    @Override
    public boolean canAccessDescription() {
        return canAccess(new DemandRight.Specification(), Action.READ);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Do things.
    // /////////////////////////////////////////////////////////////////////////////////////////

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#addContribution(java.math.BigDecimal ,
     * java.lang.String)
     */
    @Override
    public void addContribution(final BigDecimal amount, final String comment) throws NotEnoughMoneyException, UnauthorizedOperationException {
        tryAccess(new DemandRight.Contribute(), Action.WRITE);
        getDao().addContribution(getAuthToken().getMember().getDao(), amount, comment);
        setStateObject(getStateObject().eventAddContribution());
    }

    @Override
    public Offer addOffer(final Member member,
                          final BigDecimal amount,
                          final String description,
                          final Locale local,
                          final Date dateExpire,
                          final int secondsBeforeValidation) throws UnauthorizedOperationException {
        final Offer offer = new Offer(member, this, amount, description, local, dateExpire, secondsBeforeValidation);
        return doAddOffer(offer);
    }

    private Offer doAddOffer(final Offer offer) throws UnauthorizedOperationException {
        if (!offer.getDemand().equals(this)) {
            throw new IllegalArgumentException();
        }
        tryAccess(new DemandRight.Offer(), Action.WRITE);

        if (!offer.getAuthor().equals(getAuthToken().getMember())) {
            throw new UnauthorizedOperationException(SpecialCode.CREATOR_INSERTOR_MISMATCH);
        }
        getDao().addOffer(offer.getDao());
        setStateObject(getStateObject().eventAddOffer());
        return offer;
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#removeOffer(com.bloatit.model .Offer)
     */
    @Override
    public void removeOffer(final Offer offer) throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Offer(), Action.DELETE);
        if (getDao().getSelectedOffer().getId() == offer.getId()) {
            getDao().computeSelectedOffer();
        }
        getDao().removeOffer(offer.getDao());
        setStateObject(getStateObject().eventRemoveOffer(offer));
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#addComment(java.lang.String)
     */
    @Override
    public Comment addComment(final String text) throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Comment(), Action.WRITE);
        final DaoComment comment = DaoComment.createAndPersist(this.getDao(), getAuthToken().getMember().getDao(), text);
        getDao().addComment(comment);
        return Comment.create(comment);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#unSelectOffer(com.bloatit.model .Offer)
     */
    @Override
    public void unSelectOffer(final Offer offer) {
        if (offer.equals(getSelectedOfferUnprotected())) {
            setSelectedOffer(null);
            getDao().computeSelectedOffer();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#validateCurrentBatch(boolean)
     */
    @Override
    public boolean validateCurrentBatch(final boolean force) {
        throwWrongStateExceptionOnNondevelopingState();
        return getSelectedOfferUnprotected().validateCurrentBatch(force);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#cancelDevelopment()
     */
    @Override
    public void cancelDevelopment() throws UnauthorizedOperationException {
        if (!getAuthToken().getMember().equals(getSelectedOffer().getAuthor())) {
            throw new UnauthorizedOperationException(SpecialCode.NON_DEVELOPER_CANCEL_DEMAND);
        }
        setStateObject(getStateObject().eventDeveloperCanceled());
        // Work is done in the slot system.
    }

    @Override
    public void computeSelectedOffer() throws UnauthorizedOperationException {
        if (!hasUserPrivilege(Role.ADMIN)) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        getDao().computeSelectedOffer();
    }

    @Override
    public void setDemandState(FeatureState demandState) throws UnauthorizedOperationException {
        if (!hasUserPrivilege(Role.ADMIN)) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }
        setDemandStateUnprotected(demandState);
    }

    void setDemandStateUnprotected(FeatureState demandState) {
        if (getDemandState() != demandState) {
            switch (demandState) {
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
        getDao().setDemandState(FeatureState.DEVELOPPING);
        getSelectedOfferUnprotected().getCurrentBatch().setDeveloping();
        new TaskDevelopmentTimeOut(getId(), getDao().getSelectedOffer().getCurrentBatch().getExpirationDate());
    }

    /**
     * Slot called when the demand change to {@link DiscardedState}.
     */
    private void inDiscardedState() {
        getDao().setDemandState(FeatureState.DISCARDED);

        for (final Contribution contribution : getContributionsUnprotected()) {
            contribution.cancel();
        }
        getSelectedOfferUnprotected().cancelEverythingLeft();
    }

    /**
     * Slot called when this demand state change to {@link FinishedState}.
     */
    private void inFinishedState() {
        if (getDao().getSelectedOffer() == null || getDao().getSelectedOffer().hasBatchesLeft()) {
            throw new WrongStateException("Cannot be in finished state if the current offer has lots to validate.");
        }
        getDao().setDemandState(FeatureState.FINISHED);
    }

    /**
     * Slot called when this demand state change to {@link PendingState}.
     */
    private void inPendingState() {
        if (getDemandState() == FeatureState.PENDING) {
            return;
        }
        getDao().setDemandState(FeatureState.PENDING);
    }

    /**
     * Slot called when this demand state change to {@link PreparingState}.
     */
    void inPreparingState() {
        PageIterable<DaoOffer> offers = getDao().getOffers();
        if (offers.size() < 1) {
            throw new WrongStateException("There must be at least one offer to be in Preparing state.");
        }
        if (offers.size() == 1) {
            setSelectedOffer(Offer.create(offers.iterator().next()));
        } else {
            getDao().computeSelectedOffer();
        }
        getDao().setDemandState(FeatureState.PREPARING);
    }

    /**
     * Called by a {@link PlannedTask}. For now do nothing... A development
     * TimeOut is called when the expiration date arrive
     */
    void developmentTimeOut() {
        setStateObject(getStateObject().eventDevelopmentTimeOut());
    }

    /**
     * <p>
     * Test if the current demand should passe into {@link DevelopingState}.
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

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Kudosable#notifyRejected()
     */
    @Override
    protected void notifyRejected() {
        setStateObject(getStateObject().eventDemandRejected());
    }

    /**
     * Sets the selected offer. Called internally and in demandState.
     *
     * @param offer the new selected offer
     */
    void setSelectedOffer(final Offer offer) {
        final Date validationDate = DateUtils.tomorrow();
        new TaskUpdateDevelopingState(getId(), validationDate);
        getDao().setValidationDate(validationDate);
        getDao().setSelectedOffer(offer.getDao());
    }

    public void updateDevelopmentStatus() {
        updateDevelopmentState();
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
        final Offer selectedOffer = getSelectedOfferUnprotected();
        final boolean isSelectedOffer = offer.equals(selectedOffer);
        if (positif && !isSelectedOffer) {
            if (selectedOffer == null || offer.getPopularity() > selectedOffer.getPopularity()) {
                setSelectedOffer(Offer.create(offer.getDao()));
            }
        }
        if (!positif && isSelectedOffer) {
            if (selectedOffer == null || selectedOffer.getPopularity() < 0) {
                getDao().computeSelectedOffer();
            } else {
                for (final Offer thisOffer : getOffersUnprotected()) {
                    if (thisOffer.getPopularity() > selectedOffer.getPopularity()) {
                        getDao().computeSelectedOffer();
                        break;
                    }
                }
            }
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
     * Tell that the current batch is validate. This method is called by
     * {@link Offer} when needed.
     */
    public void setBatchIsValidated() {
        setStateObject(getStateObject().eventBatchIsValidated());
    }

    /**
     * Tell that the current batch is rejected. This method is called by
     * {@link Offer} when needed.
     */
    public void setBatchIsRejected() {
        setStateObject(getStateObject().eventBatchIsRejected());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Get something
    // /////////////////////////////////////////////////////////////////////////////////////////

    public boolean isDeveloping() {
        boolean isDeveloping = getDemandState() == FeatureState.DEVELOPPING;
        if (isDeveloping) {
            assert getSelectedOfferUnprotected() != null;
            assert getValidatedOfferUnprotected() != null;
            assert getSelectedOfferUnprotected().equals(getValidatedOfferUnprotected());
            assert getValidatedOfferUnprotected().isFinished() == false;
        }
        return isDeveloping;
    }

    private void throwWrongStateExceptionOnNondevelopingState() {
        if (!isDeveloping()) {
            throw new WrongStateException("Demand should be in Developing state.");
        }
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getValidationDate()
     */
    @Override
    public Date getValidationDate() {
        return getDao().getValidationDate();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getComments()
     */
    @Override
    public PageIterable<Comment> getComments() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Comment(), Action.READ);
        return new CommentList(getDao().getComments());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getContributions()
     */
    @Override
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Contribute(), Action.READ);
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

    /** The Constant PROGRESSION_COEF. */
    private static final int PROGRESSION_COEF = 42;

    /** The Constant PROGRESSION_CONTRIBUTION_DIVISOR. */
    private static final int PROGRESSION_CONTRIBUTION_DIVISOR = 200;

    /** The Constant PROGRESSION_PERCENT. */
    public static final int PROGRESSION_PERCENT = 100;

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getProgression()
     */
    @Override
    public float getProgression() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Contribute(), Action.READ);
        final Offer currentOffer = getSelectedOffer();
        if (currentOffer == null) {
            return PROGRESSION_COEF * (1 - 1 / (1 + getDao().getContribution().floatValue() / PROGRESSION_CONTRIBUTION_DIVISOR));
        }
        return currentOffer.getProgression();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getContribution()
     */
    @Override
    public BigDecimal getContribution() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Contribute(), Action.READ);
        return getDao().getContribution();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getContributionMax()
     */
    @Override
    public BigDecimal getContributionMax() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Contribute(), Action.READ);
        return getDao().getContributionMax();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getContributionMin()
     */
    @Override
    public BigDecimal getContributionMin() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Contribute(), Action.READ);
        return getDao().getContributionMin();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getDescription()
     */
    @Override
    public Description getDescription() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Description(), Action.READ);
        return Description.create(getDao().getDescription());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getProject()
     */
    @Override
    public Project getProject() throws UnauthorizedOperationException {
        // TODO: access right
        return Project.create(getDao().getProject());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getOffers()
     */
    @Override
    public PageIterable<Offer> getOffers() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Offer(), Action.READ);
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

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getSelectedOffer()
     */
    @Override
    public Offer getSelectedOffer() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Offer(), Action.READ);
        return getSelectedOfferUnprotected();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getValidatedOffer()
     */
    @Override
    public Offer getValidatedOffer() throws UnauthorizedOperationException {
        tryAccess(new DemandRight.Offer(), Action.READ);
        return getValidatedOfferUnprotected();
    }

    private Offer getValidatedOfferUnprotected() {
        if (getDao().getSelectedOffer() != null && getValidationDate().before(new Date())) {
            return getSelectedOfferUnprotected();
        }
        return null;
    }

    /**
     * Gets the selected offer with no Right management.
     *
     * @return the selected offer unprotected
     */
    private Offer getSelectedOfferUnprotected() {
        return Offer.create(getDao().getSelectedOffer());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getTitle()
     */
    @Override
    public String getTitle() throws UnauthorizedOperationException {
        return getDescription().getDefaultTranslation().getTitle();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.Demand#getDemandState()
     */
    @Override
    public FeatureState getDemandState() {
        return getDao().getDemandState();
    }

    /**
     * Sets the state object.
     *
     * @param stateObject the new state object
     */
    private void setStateObject(final AbstractDemandState stateObject) {
        this.stateObject = stateObject;
    }

    /**
     * Gets the state object.
     *
     * @return the state object
     */
    private AbstractDemandState getStateObject() {

        switch (getDao().getDemandState()) {
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
        return ModelConfiguration.getKudosableDemandTurnPending();
    }

    /**
     * Turn valid.
     *
     * @return the int
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return ModelConfiguration.getKudosableDemandTurnValid();
    }

    /**
     * Turn rejected.
     *
     * @return the int
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return ModelConfiguration.getKudosableDemandTurnRejected();
    }

    /**
     * Turn hidden.
     *
     * @return the int
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return ModelConfiguration.getKudosableDemandTurnHidden();
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
