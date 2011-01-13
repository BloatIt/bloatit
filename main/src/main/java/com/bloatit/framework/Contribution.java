package com.bloatit.framework;

import java.math.BigDecimal;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoContribution;
import com.bloatit.model.data.DaoContribution.State;
import com.bloatit.model.data.DaoUserContent;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * This is a financial contribution.
 *
 * @see DaoContribution
 */
public final class Contribution extends UserContent {

    private final DaoContribution dao;

    /**
     * Create a <code>Contribution</code> or return null (if dao is null)
     */
    public static Contribution create(final DaoContribution dao) {
        if (dao != null) {
            return new Contribution(dao);
        }
        return null;
    }

    private Contribution(final DaoContribution dao) {
        super();
        this.dao = dao;
    }

    /**
     * You have to call {@link #accept(Offer)} when an offer is accepted. This will create
     * the {@link Transaction} needed so that the developer of the offer become rich.
     *
     * @param offer the validated offer.
     * @throws NotEnoughMoneyException if there is a bug and then a person does not have
     *         enough money.
     */
    protected void accept(final Offer offer) throws NotEnoughMoneyException {
        dao.accept(offer.getDao());
    }

    /**
     * You have to call {@link #cancel()} when the demand on which this Contribution is
     * made is canceled. It allows the user to take back its money.
     */
    protected void cancel() {
        dao.cancel();
    }

    public BigDecimal getAmount() {
        return dao.getAmount();
    }

    public State getState() {
        return dao.getState();
    }

    /**
     * return true if you can access the <code>Transaction</code> property.
     *
     * @see #getTransaction()
     * @see Contribution#authenticate(AuthToken)
     */
    public boolean canAccessTransaction() {
        return new MoneyRight.Everything().canAccess(calculateRole(this), Action.READ);
    }

    /**
     * @return the transaction associated with this Contribution. It can be null (for
     *         example, if the transaction is not done yet).
     * @throws UnauthorizedOperationException
     */
    public Transaction getTransaction() throws UnauthorizedOperationException {
        new MoneyRight.Everything().tryAccess(calculateRole(this), Action.READ);
        return new Transaction(dao.getTransaction());
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }

    protected DaoContribution getDao() {
        return dao;
    }

}
