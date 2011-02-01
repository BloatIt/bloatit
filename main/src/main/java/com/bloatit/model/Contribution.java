package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.right.ContributionRight;
import com.bloatit.model.right.RightManager.Action;

/**
 * This is a financial contribution.
 * 
 * @see DaoContribution
 */
public final class Contribution extends UserContent<DaoContribution> {

    /**
     * Create a <code>Contribution</code> or return null (if dao is null)
     */
    public static Contribution create(final DaoContribution dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoContribution> created = CacheManager.get(dao);
            if (created == null) {
                return new Contribution(dao);
            }
            return (Contribution) created;
        }
        return null;
    }

    private Contribution(final DaoContribution dao) {
        super(dao);
    }

    /**
     * CALLED by demand. You have to call {@link #accept(Offer)} when an offer is
     * accepted. This will create the {@link Transaction} needed so that the developer of
     * the offer become rich.
     * 
     * @param offer the validated offer.
     * @throws NotEnoughMoneyException if there is a bug and then a person does not have
     *         enough money.
     */
    public void accept(final Offer offer) throws NotEnoughMoneyException {
        dao.validate(offer.getDao(), 100);
    }

    /**
     * CALLED by demand. You have to call {@link #cancel()} when the demand on which this
     * Contribution is made is canceled. It allows the user to take back its money.
     */
    public void cancel() {
        dao.cancel();
    }

    /**
     * return true if you can access the <code>Amount</code> property.
     * 
     * @see #getAmount()()
     * @see Contribution#authenticate(AuthToken)
     */
    public boolean canAccessAmount() {
        return new ContributionRight.Amount().canAccess(calculateRole(this), Action.READ);
    }

    /**
     * @return the amount.
     * @throws UnauthorizedOperationException if you do not have the right to access the
     *         <code>Amount</code> property.
     * @see Contribution#authenticate(AuthToken)
     */
    public BigDecimal getAmount() throws UnauthorizedOperationException {
        new ContributionRight.Amount().tryAccess(calculateRole(this), Action.READ);
        return dao.getAmount();
    }

    /**
     * return true if you can access the <code>Comment</code> property.
     * 
     * @see #getComment()()
     * @see Contribution#authenticate(AuthToken)
     */
    public boolean canAccessComment() {
        return new ContributionRight.Comment().canAccess(calculateRole(this), Action.READ);
    }

    /**
     * @return the comment.
     * @throws UnauthorizedOperationException if you do not have the right to access the
     *         <code>Comment</code> property.
     */
    public String getComment() throws UnauthorizedOperationException {
        new ContributionRight.Comment().tryAccess(calculateRole(this), Action.READ);
        return dao.getComment();
    }

    protected DaoContribution getDao() {
        return dao;
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }
}
