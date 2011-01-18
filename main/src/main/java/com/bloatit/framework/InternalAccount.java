package com.bloatit.framework;

import java.math.BigDecimal;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.Log;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoInternalAccount;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * An internal account is an account containing the money we store for a user. There is
 * one internal account for each user and only one. An internal account can never have an
 * amount under zero. An internal account can have some money blocked. When you contribute
 * on an idea, you do not spend the money directly, but it is blocked and you cannot use
 * it elsewhere.
 * 
 * @author tguyard
 */
public final class InternalAccount extends Account {

    private final DaoInternalAccount dao;

    protected InternalAccount(final DaoInternalAccount dao) {
        super();
        this.dao = dao;
    }

    public DaoInternalAccount getDao() {
        return dao;
    }

    /**
     * Return the amount blocked into contribution on non finished idea.
     * 
     * @return a positive bigdecimal.
     * @throws UnauthorizedOperationException
     */
    public BigDecimal getBlocked() throws UnauthorizedOperationException {
        new MoneyRight.Transaction().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return dao.getBlocked();
    }

    /**
     * This was not meant to be used like this. First : Charge amount is not generic. The
     * operation done here is just a transfer between 2 account. Second : If there is a
     * {@link NotEnoughMoneyException} it is to be used.
     * 
     * @param amount
     * @param externalAccount
     */
    @Deprecated
    public void chargeAmount(final BigDecimal amount, final Account externalAccount) {
        try {
            new Transaction(this, externalAccount, amount.negate());
        } catch (final NotEnoughMoneyException ex) {
            // Should never happen
            Log.web().fatal(ex);
            throw new FatalErrorException("Ooops ... Looks like you fell in a warphole ... you should never see this", ex);
        }
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return dao;
    }

}
