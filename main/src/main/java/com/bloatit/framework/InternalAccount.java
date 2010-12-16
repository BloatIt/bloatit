package com.bloatit.framework;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.exceptions.NotEnoughMoneyException;
import java.math.BigDecimal;

import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoInternalAccount;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalAccount extends Account {

    private final DaoInternalAccount dao;

    protected InternalAccount(final DaoInternalAccount dao) {
        super();
        this.dao = dao;
    }

    public DaoInternalAccount getDao() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return dao;
    }

    public BigDecimal getBlocked() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return dao.getBlocked();
    }

    public void chargeAmount(BigDecimal amount, Account externalAccount) {
        try {
            // Amount is negated cause it's a transaction FROM an external account
            // TO an internal account
            Transaction transac = new Transaction(this, externalAccount, amount.negate());
        } catch (NotEnoughMoneyException ex) {
            // Should never happen
            throw new FatalErrorException("Ooops ... Looks like you fell in a warphole ... you should never see this", ex);
        }
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return dao;
    }

}
