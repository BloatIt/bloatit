package com.bloatit.model.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.exceptions.WrongStateException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.AuthToken;
import com.bloatit.model.CacheManager;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Description;
import com.bloatit.model.Identifiable;
import com.bloatit.model.Kudosable;
import com.bloatit.model.KudosableConfiguration;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.PlannedTask;
import com.bloatit.model.Project;
import com.bloatit.model.lists.CommentList;
import com.bloatit.model.lists.ContributionList;
import com.bloatit.model.lists.OfferList;
import com.bloatit.model.right.DemandRight;
import com.bloatit.model.right.RightManager.Action;

// TODO : delete comment.
//

/**
 * A demand is an idea :)
 */
public final class Demand extends Kudosable<DaoDemand> {
    private AbstractDemandState stateObject;

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Create a new Demand. This method is not protected by any right management.
     *
     * @return null if the <code>dao</code> is null.
     */
    public static Demand create(final DaoDemand dao) {
        if (dao == null || !SessionManager.getSessionFactory().getCurrentSession().contains(dao)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Identifiable<DaoDemand> created = CacheManager.get(dao);
        if (created == null) {
            return new Demand(dao);
        }
        return (Demand) created;
    }

    /**
     * Create a new demand. The right management for creating a demand is specific. (The
     * Right management system is not working in this case). You have to use the
     * {@link DemandManager#canCreate(AuthToken)} to make sure you can create a new
     * demand.
     *
     * @see DaoDemand#DaoDemand(Member,Locale,String, String)
     */
    public Demand(final Member author, final Locale locale, final String title, final String description, final Project project) {
        this(DaoDemand.createAndPersist(author.getDao(),
                                        DaoDescription.createAndPersist(author.getDao(), locale, title, description),
                                        project.getDao()));
    }

    /**
     * Use the {@link #create(DaoDemand)} method.
     */
    private Demand(final DaoDemand dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Can something
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param action is the type of action you can do on the property. (READ for the
     *        getter, WRITE for the SETTER etc.)
     * @return true if you can access the <code>Comment</code> property.
     * @see #getComments()
     * @see #addComment(String)
     */
    public boolean canAccessComment(final Action action) {
        return new DemandRight.Comment().canAccess(calculateRole(this), action);
    }

    /**
     * @param action is the type of action you can do on the property. (READ for the
     *        getter, WRITE for the SETTER etc.)
     * @return true if you can access the <code>Contribution</code> property.
     * @see #getContribution()
     * @see #getContributionMax()
     * @see #getContributionMin()
     * @see #getContributions()
     * @see #addContribution(BigDecimal, String)
     */
    public boolean canAccessContribution(final Action action) {
        return new DemandRight.Contribute().canAccess(calculateRole(this), action);
    }

    /**
     * @param action is the type of action you can do on the property. (READ for the
     *        getter, WRITE for the SETTER etc.)
     * @return true if you can access the <code>Offer</code> property.
     * @see #getOffers()
     * @see #addOffer(BigDecimal, Locale, String, String, Date)
     */
    public boolean canAccessOffer(final Action action) {
        return new DemandRight.Offer().canAccess(calculateRole(this), action);
    }

    /**
     * @return true if you can access the <code>Description</code> property.
     * @see #getDescription()
     */
    public boolean canAccessDescription() {
        return new DemandRight.Specification().canAccess(calculateRole(this), Action.READ);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Do things.
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a contribution on this demand.
     *
     * @param amount must be a positive non null value.
     * @param comment can be null or empty and should be less than 140 char long.
     * @throws NotEnoughMoneyException if the person logged does not have enough money to
     *         make this contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#WRITE} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public void addContribution(final BigDecimal amount, final String comment) throws NotEnoughMoneyException, UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.WRITE);
        getDao().addContribution(getAuthToken().getMember().getDao(), amount, comment);
        setStateObject(getStateObject().eventAddContribution());
    }

    /**
     * Add a new Offer on this Demand. You can do this operation when you are in the
     * {@link DemandState#PENDING} or {@link DemandState#PREPARING} DemandState. When you
     * add the first Offer, the state pass from {@link DemandState#PENDING} to
     * {@link DemandState#PREPARING}; and this offer is selected (see
     * {@link DaoDemand#setSelectedOffer(DaoOffer)}). The parameters of this function are
     * used to create the first (non optional) batch in this offer.
     *
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#WRITE} right on the <code>Offer</code> property.
     * @throws WrongStateException if the state is != from {@link DemandState#PENDING} or
     *         {@link DemandState#PREPARING}.
     * @see #authenticate(AuthToken)
     */
    public void addOffer(final Offer offer) throws UnauthorizedOperationException {
        if (offer == null) {
            throw new NonOptionalParameterException();
        }
        if (!offer.getDemand().equals(this)) {
            throw new IllegalArgumentException();
        }

        new DemandRight.Offer().tryAccess(calculateRole(this), Action.WRITE);
        if (!offer.getAuthor().equals(getAuthToken().getMember())) {
            throw new UnauthorizedOperationException(SpecialCode.CREATOR_INSERTOR_MISMATCH);
        }
        setStateObject(getStateObject().eventAddOffer(offer));
        getDao().addOffer(offer.getDao());
    }

    /**
     * For now only the admin can delete an offer.
     *
     * @param offer is the offer to delete.
     * @throws UnauthorizedOperationException if the user does not has the
     *         <code>DELETE</code> right on the <code>Offer</code> property.
     * @see #authenticate(AuthToken)
     */
    public void removeOffer(final Offer offer) throws UnauthorizedOperationException {
        new DemandRight.Offer().tryAccess(calculateRole(this), Action.DELETE);
        if (getDao().getSelectedOffer().getId() == offer.getId()) {
            getDao().computeSelectedOffer();
        }
        setStateObject(getStateObject().eventRemoveOffer(offer));
        getDao().removeOffer(offer.getDao());
    }

    /**
     * Works only in development state.
     *
     * @throws UnauthorizedOperationException If this is not the current developer thats
     *         try to cancel the dev.
     */
    public void cancelDevelopment() throws UnauthorizedOperationException {
        if (!getAuthToken().getMember().equals(getSelectedOffer().getAuthor())) {
            throw new UnauthorizedOperationException(SpecialCode.NON_DEVELOPER_CANCEL_DEMAND);
        }
        cancel();
        setStateObject(getStateObject().eventDeveloperCanceled());
    }

    /**
     * Cancel all the contribution on this demand.
     */
    private void cancel() {
        for (final Contribution contribution : getContributionsUnprotected()) {
            contribution.cancel();
        }
        // Maybe I should make sure everything is canceled in every Offer/batches ?
    }

    public void releaseCurrentBatch() throws UnauthorizedOperationException {
        if (!getAuthToken().getMember().equals(getSelectedOffer().getAuthor())) {
            throw new UnauthorizedOperationException(SpecialCode.NON_DEVELOPER_FINISHED_DEMAND);
        }
        if (getSelectedOfferUnprotected().isFinished()) {
            throw new FatalErrorException("There is no batch left for this Offer !");
        }

        setStateObject(getStateObject().eventBatchReleased());
        // The offer really don't care to know if the current batch is under development
        // or not.
    }

    // TODO authorization
    public boolean validateCurrentBatch(final boolean force) {
        if (getSelectedOfferUnprotected().isFinished()) {
            throw new FatalErrorException("There is no batch left for this Offer !");
        }
        if (getDemandState() != DemandState.INCOME) {
            throw new WrongStateException();
        }
        return getSelectedOfferUnprotected().validateCurrentBatch(force);
    }

    /**
     * Add a comment at the end of the comment list.
     *
     * @param text is the text of the comment.
     * @throws UnauthorizedOperationException if you do not have the {@link Action#WRITE}
     *         right on the <code>Comment</code> property.
     * @see #authenticate(AuthToken)
     */
    public void addComment(final String text) throws UnauthorizedOperationException {
        new DemandRight.Comment().tryAccess(calculateRole(this), Action.WRITE);
        getDao().addComment(DaoComment.createAndPersist(getAuthToken().getMember().getDao(), text));
    }

    public void notifyOfferKudos(final Offer offer, final boolean positif) {
        final boolean isSelectedOffer = offer.equals(getSelectedOfferUnprotected());
        if (positif && !isSelectedOffer) {
            if (offer.getPopularity() > getSelectedOfferUnprotected().getPopularity()) {
                getDao().setSelectedOffer(offer.getDao());
            }
        }
        if (!positif && isSelectedOffer) {
            for (final Offer thisOffer : getOffersUnprotected()) {
                if (thisOffer.getPopularity() > getSelectedOfferUnprotected().getPopularity()) {
                    getDao().computeSelectedOffer();
                }

            }
        }
    }

    /**
     * Used by Offer class. You should never have to use it
     *
     * @param offer the offer to unselect. Nothing is done if the offer is not selected.
     */
    public void unSelectOffer(final Offer offer) {
        if (offer.equals(getSelectedOfferUnprotected())) {
            setSelectedOffer(null);
            getDao().computeSelectedOffer();
        }

    }

    // ////////////////////////////////////////////////////////////////////////
    // Slots and notification system
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Tells that we are in development state.
     */
    void inDevelopmentState() {
        getDao().setDemandState(DemandState.DEVELOPPING);
        new TaskDevelopmentTimeOut(this.getId(), getDao().getSelectedOffer().getCurrentBatch().getExpirationDate());
    }

    /**
     * Slot called when the demand change to {@link DiscardedState}.
     */
    void inDiscardedState() {
        getDao().setDemandState(DemandState.DISCARDED);
    }

    /**
     * Slot called when this demand state change to {@link FinishedState}.
     */
    void inFinishedState() {
        getDao().setDemandState(DemandState.FINISHED);
    }

    /**
     * Slot called when this demand state change to {@link IncomeState}.
     */
    void inIncomeState() {
        getDao().setDemandState(DemandState.INCOME);

    }

    /**
     * Slot called when this demand state change to {@link PendingState}.
     */
    void inPendingState() {
        getDao().setDemandState(DemandState.PENDING);

    }

    /**
     * Slot called when this demand state change to {@link PreparingState}.
     */
    void inPreparingState() {
        getDao().setDemandState(DemandState.PREPARING);
    }

    /**
     * Called by a {@link PlannedTask}
     */
    void developmentTimeOut() {
        setStateObject(getStateObject().eventDevelopmentTimeOut());
    }

    /**
     * Called by a {@link PlannedTask}
     */
    void selectedOfferTimeOut() {
        setStateObject(getStateObject().eventSelectedOfferTimeOut(getDao().getContribution()));
    }

    @Override
    protected void notifyValid() {
        if (getStateObject().getState() == DemandState.DISCARDED) {
            setStateObject(getStateObject().eventPopularityPending());
        }
    }

    @Override
    protected void notifyPending() {
        if (getStateObject().getState() == DemandState.DISCARDED) {
            setStateObject(getStateObject().eventPopularityPending());
        }
    }

    @Override
    protected void notifyRejected() {
        setStateObject(getStateObject().eventDemandRejected());
    }

    void setSelectedOffer(final Offer offer) {
        final Date validationDate = DateUtils.tomorrow();
        new TaskSelectedOfferTimeOut(this.getId(), validationDate);
        this.getDao().setValidationDate(validationDate);
        this.getDao().setSelectedOffer(offer.getDao());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Offer feedBack
    // /////////////////////////////////////////////////////////////////////////////////////////

    public void setOfferIsValidated() {
        setStateObject(getStateObject().eventOfferIsValidated());
    }

    public void setBatchIsValidated() {
        setStateObject(getStateObject().eventBatchIsValidated());
    }

    public void setBatchIsRejected() {
        setStateObject(getStateObject().eventBatchIsRejected());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Get something
    // /////////////////////////////////////////////////////////////////////////////////////////

    public Date getValidationDate() {
        return getDao().getValidationDate();
    }

    /**
     * @return the first level comments on this demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Comment</code> property.
     * @see #authenticate(AuthToken)
     */
    public PageIterable<Comment> getComments() throws UnauthorizedOperationException {
        new DemandRight.Comment().tryAccess(calculateRole(this), Action.READ);
        return new CommentList(getDao().getCommentsFromQuery());
    }

    /**
     * @return all the Contributions on this Demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return getContributionsUnprotected();
    }

    /**
     * @see #getContribution()
     */
    private PageIterable<Contribution> getContributionsUnprotected() {
        return new ContributionList(getDao().getContributionsFromQuery());
    }

    private static final int PROGRESSION_COEF = 42;
    private static final int PROGRESSION_CONTRIBUTION_DIVISOR = 200;
    public static final int PROGRESSION_PERCENT = 100;

    /**
     * Return the progression in percent. It compare the amount of contribution to the
     * amount of the current offer.
     *
     * @return a percentage. It can be > 100 if the amount of contributions is greater
     *         than the amount for the current offer. If the offer amount is 0 then it
     *         return Float.POSITIVE_INFINITY.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public float getProgression() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        final DaoOffer currentOffer = getDao().getSelectedOffer();
        if (getDao().getOffers().isEmpty() || currentOffer == null) {
            return PROGRESSION_COEF * (1 - 1 / (1 + getDao().getContribution().floatValue() / PROGRESSION_CONTRIBUTION_DIVISOR));
        }
        if (currentOffer.getAmount().floatValue() != 0) {
            return (getDao().getContribution().floatValue() * PROGRESSION_PERCENT) / currentOffer.getAmount().floatValue();
        }
        return Float.POSITIVE_INFINITY;
    }

    /**
     * @return return the sum of the values of all the contributions on this demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public BigDecimal getContribution() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return getDao().getContribution();
    }

    /**
     * @return return the value of the contribution with the max contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public BigDecimal getContributionMax() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return getDao().getContributionMax();
    }

    /**
     * @return return the value of the contribution with the min contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public BigDecimal getContributionMin() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return getDao().getContributionMin();
    }

    /**
     * @return the current Description of this offer.
     * @throws UnauthorizedOperationException if the user does not has the right on the
     *         <code>Description</code> property.
     * @see #authenticate(AuthToken)
     */
    public Description getDescription() throws UnauthorizedOperationException {
        new DemandRight.Description().tryAccess(calculateRole(this), Action.READ);
        return Description.create(getDao().getDescription());
    }

    /**
     * @return all the offers on this demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         <code>READ</code> right on the <code>Offer</code> property.
     * @see #authenticate(AuthToken)
     */
    public PageIterable<Offer> getOffers() throws UnauthorizedOperationException {
        new DemandRight.Offer().tryAccess(calculateRole(this), Action.READ);
        return getOffersUnprotected();
    }

    private PageIterable<Offer> getOffersUnprotected() {
        return new OfferList(getDao().getOffersFromQuery());
    }

    /**
     * The current offer is the offer with the max popularity then the min amount.
     *
     * @return the current offer for this demand, or null if there is no offer.
     * @throws UnauthorizedOperationException if the user does not has the
     *         <code>READ</code> right on the <code>Offer</code> property.
     * @see #authenticate(AuthToken)
     */
    public Offer getSelectedOffer() throws UnauthorizedOperationException {
        new DemandRight.Offer().tryAccess(calculateRole(this), Action.READ);
        return getSelectedOfferUnprotected();
    }

    /**
     * A validated offer is an offer selected for more than one day. (If you are in
     * {@link DemandState#DEVELOPPING} state then there should be always a validated
     * offer.
     *
     * @return the validated offer or null if there is no valid offer.
     * @throws UnauthorizedOperationException if you do not have the <code>READ</code>
     *         right on the offer property
     */
    public Offer getValidatedOffer() throws UnauthorizedOperationException {
        new DemandRight.Offer().tryAccess(calculateRole(this), Action.READ);
        if (getDao().getSelectedOffer() != null && getValidationDate().before(new Date())) {
            return getSelectedOfferUnprotected();
        }
        return null;
    }

    private Offer getSelectedOfferUnprotected() {
        return Offer.create(getDao().getSelectedOffer());
    }

    /**
     * @throws UnauthorizedOperationException if the user does not has the
     *         <code>READ</code> right on the <code>Description</code> property.
     * @see #authenticate(AuthToken)
     * @see #getDescription()
     */
    public String getTitle() throws UnauthorizedOperationException {
        return getDescription().getDefaultTranslation().getTitle();
    }

    public DemandState getDemandState() {
        return getDao().getDemandState();
    }

    public void setStateObject(final AbstractDemandState stateObject) {
        this.stateObject = stateObject;
    }

    public AbstractDemandState getStateObject() {
        switch (getDao().getDemandState()) {
        case PENDING:
            if (stateObject == null || !stateObject.getClass().equals(PendingState.class)) {
                setStateObject(new PendingState(this));
            }
            break;
        case DEVELOPPING:
            if (stateObject == null || !stateObject.getClass().equals(DeveloppingState.class)) {
                setStateObject(new DeveloppingState(this));
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
        case INCOME:
            if (stateObject == null || !stateObject.getClass().equals(IncomeState.class)) {
                setStateObject(new IncomeState(this));
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
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return KudosableConfiguration.getDemandTurnPending();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return KudosableConfiguration.getDemandTurnValid();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return KudosableConfiguration.getDemandTurnRejected();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return KudosableConfiguration.getDemandTurnHidden();
    }

}
