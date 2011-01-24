package com.bloatit.framework.demand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.common.DateUtils;
import com.bloatit.common.PageIterable;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.common.UnauthorizedOperationException.SpecialCode;
import com.bloatit.common.WrongDemandStateException;
import com.bloatit.framework.AuthToken;
import com.bloatit.framework.Comment;
import com.bloatit.framework.Contribution;
import com.bloatit.framework.Description;
import com.bloatit.framework.Kudosable;
import com.bloatit.framework.Member;
import com.bloatit.framework.Offer;
import com.bloatit.framework.PlannedTask;
import com.bloatit.framework.lists.CommentList;
import com.bloatit.framework.lists.ContributionList;
import com.bloatit.framework.lists.OfferList;
import com.bloatit.framework.right.DemandRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoDemand.DemandState;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoOffer;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A demand is an idea :)
 */
public final class Demand extends Kudosable {
    private final DaoDemand dao;
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
        return new Demand(dao);
    }

    /**
     * Create a new demand. The right management for creating a demand is specific. (The
     * Right management system is not working in this case). You have to use the
     * {@link DemandManager#canCreate(AuthToken)} to make sure you can create a new
     * demand.
     *
     * @see DaoDemand#DaoDemand(Member,Locale,String, String)
     */
    public Demand(final Member author, final Locale locale, final String title, final String description) {
        this(DaoDemand.createAndPersist(author.getDao(), DaoDescription.createAndPersist(author.getDao(), locale, title, description)));
    }

    /**
     * Use the {@link #create(DaoDemand)} method.
     */
    private Demand(final DaoDemand dao) {
        super();
        this.dao = dao;
        switch (dao.getDemandState()) {
        case PENDING:
            stateObject = new PendingState(this);
            break;
        case DEVELOPPING:
            stateObject = new DeveloppingState(this);
            break;
        case DISCARDED:
            stateObject = new DiscardedState(this);
            break;
        case FINISHED:
            stateObject = new FinishedState(this);
            break;
        case INCOME:
            stateObject = new IncomeState(this);
            break;
        case PREPARING:
            stateObject = new PreparingState(this);
            break;
        default:
            assert false;
            break;
        }
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
     * @param action is the type of action you can do on the property. (<code>READ</code>
     *        for the getter, <code>WRITE</code> for the SETTER etc.)
     * @return true if you can access the <code>Specification</code> property.
     * @see #createSpecification(String)
     * @see #getSpecification()
     */
    public boolean canAccessSpecification(final Action action) {
        return new DemandRight.Specification().canAccess(calculateRole(this), action);
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
        stateObject = stateObject.addContribution(amount, Offer.create(dao.getSelectedOffer()));
        dao.addContribution(getAuthToken().getMember().getDao(), amount, comment);
    }

    /**
     * Add a new Offer on this Demand. You can do this operation when you are in the
     * {@link DemandState#PENDING} or {@link DemandState#PREPARING} DemandState. When you
     * add the first Offer, the state pass from {@link DemandState#PENDING} to
     * {@link DemandState#PREPARING}; and this offer is selected (see
     * {@link DaoDemand#setSelectedOffer(DaoOffer)}).
     *
     * @param amount must be positive (can be ZERO) non null.
     * @param locale must be non null. Is the locale in which the title and the text are
     *        written.
     * @param title is the title of the offer. Must be non null.
     * @param text is the description of the offer. Must be non null.
     * @param dateExpir is the date when this offer should be finished. Must be non null.
     *        Must be in the future.
     * @return the newly created offer.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#WRITE} right on the <code>Offer</code> property.
     * @throws WrongDemandStateException if the state is != from
     *         {@link DemandState#PENDING} or {@link DemandState#PREPARING}.
     * @see #authenticate(AuthToken)
     */
    public Offer addOffer(final BigDecimal amount, final Locale locale, final String title, final String text, final Date dateExpir)
            throws UnauthorizedOperationException {
        new DemandRight.Offer().tryAccess(calculateRole(this), Action.WRITE);
        Offer offer = Offer.create(dao.addOffer(getAuthToken().getMember().getDao(), amount, new Description(getAuthToken().getMember(), locale,
                title, text).getDao(), dateExpir));
        stateObject = stateObject.addOffer(offer);
        return offer;
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
        if (dao.getSelectedOffer().getId() == offer.getId()) {
            dao.computeSelectedOffer();
        }
        stateObject = stateObject.removeOffer(offer);
        dao.removeOffer(offer.getDao());
    }

    /**
     * Works only in development state.
     *
     * @throws UnauthorizedOperationException If this is not the current developer thats
     *         try to cancel the dev.
     */
    public void CancelDevelopment() throws UnauthorizedOperationException {
        if (!getAuthToken().getMember().equals(getSelectedOffer().getAuthor())) {
            throw new UnauthorizedOperationException(SpecialCode.NON_DEVELOPER_CANCEL_DEMAND);
        }
        stateObject = stateObject.developerCanceled();
    }

    public void setSelectedOfferTimeOut() {
        stateObject = stateObject.selectedOfferTimeOut(dao.getContribution(), Offer.create(dao.getSelectedOffer()));
    }

    @Override
    protected void notifyValid() {
        if (stateObject.getState() == DemandState.DISCARDED) {
            stateObject = stateObject.popularityPending();
        }
    }

    @Override
    protected void notifyPending() {
        if (stateObject.getState() == DemandState.DISCARDED) {
            stateObject = stateObject.popularityPending();
        }
    }

    @Override
    protected void notifyRejected() {
        stateObject = stateObject.popularityTooLow();
    }

    void setSelectedOffer(Offer offer) {
        if (!PlannedTask.updatePlanedTask(TaskSelectedOfferTimeOut.class, getId(), DateUtils.tomorrow())) {
            new TaskSelectedOfferTimeOut(this, DateUtils.tomorrow());
        }
        this.dao.setSelectedOffer(offer.getDao());
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
        dao.addComment(DaoComment.createAndPersist(getAuthToken().getMember().getDao(), text));
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Get something
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the first level comments on this demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Comment</code> property.
     * @see #authenticate(AuthToken)
     */
    public PageIterable<Comment> getComments() throws UnauthorizedOperationException {
        new DemandRight.Comment().tryAccess(calculateRole(this), Action.READ);
        return new CommentList(dao.getCommentsFromQuery());
    }

    /**
     * @return all the Contributions on this Demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public PageIterable<Contribution> getContributions() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return new ContributionList(dao.getContributionsFromQuery());
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
        final DaoOffer currentOffer = dao.getSelectedOffer();
        if (dao.getOffers().isEmpty() || currentOffer == null) {
            return PROGRESSION_COEF * (1 - 1 / (1 + dao.getContribution().floatValue() / PROGRESSION_CONTRIBUTION_DIVISOR));
        }
        if (currentOffer.getAmount().floatValue() != 0) {
            return (dao.getContribution().floatValue() * PROGRESSION_PERCENT) / currentOffer.getAmount().floatValue();
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
        return dao.getContribution();
    }

    /**
     * @return return the value of the contribution with the max contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public BigDecimal getContributionMax() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return dao.getContributionMax();
    }

    /**
     * @return return the value of the contribution with the min contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *         {@link Action#READ} right on the <code>Contribution</code> property.
     * @see #authenticate(AuthToken)
     */
    public BigDecimal getContributionMin() throws UnauthorizedOperationException {
        new DemandRight.Contribute().tryAccess(calculateRole(this), Action.READ);
        return dao.getContributionMin();
    }

    /**
     * @return the current Description of this offer.
     * @throws UnauthorizedOperationException if the user does not has the right on the
     *         <code>Description</code> property.
     * @see #authenticate(AuthToken)
     */
    public Description getDescription() throws UnauthorizedOperationException {
        new DemandRight.Description().tryAccess(calculateRole(this), Action.READ);
        return Description.create(dao.getDescription());
    }

    /**
     * @return all the offers on this demand.
     * @throws UnauthorizedOperationException if the user does not has the
     *         <code>READ</code> right on the <code>Offer</code> property.
     * @see #authenticate(AuthToken)
     */
    public PageIterable<Offer> getOffers() throws UnauthorizedOperationException {
        new DemandRight.Offer().tryAccess(calculateRole(this), Action.READ);
        return new OfferList(dao.getOffersFromQuery());
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
        return Offer.create(dao.getSelectedOffer());
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

    /**
     * @return the dao object of this Demand.
     */
    protected DaoDemand getDao() {
        return dao;
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
