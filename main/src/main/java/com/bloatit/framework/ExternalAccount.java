package com.bloatit.framework;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.ExternalAccountRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoExternalAccount.AccountType;

/**
 * @see DaoExternalAccount
 */
public final class ExternalAccount extends Account {

    private final DaoExternalAccount dao;

    public ExternalAccount(final Actor actor, final AccountType type, final String bankCode) {
        dao = DaoExternalAccount.createAndPersist(actor.getDao(), type, bankCode);
    }

    protected ExternalAccount(final DaoExternalAccount dao) {
        super();
        this.dao = dao;
    }

    protected DaoExternalAccount getDao() {
        return dao;
    }

    /**
     * @return true if you can access the <code>BankCode</code> property.
     */
    public boolean canAccessBankCode() {
        return new ExternalAccountRight.BankCode().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if you can access the <code>Type</code> property.
     */
    public boolean canAccessType() {
        return new ExternalAccountRight.Type().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @throws UnauthorizedOperationException if you do not have the right to access the
     *         <code>BankCode</code> property.
     */
    public String getBankCode() throws UnauthorizedOperationException {
        new ExternalAccountRight.BankCode().tryAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
        return dao.getBankCode();
    }

    /**
     * @throws UnauthorizedOperationException if you do not have the right to access the
     *         <code>Type</code> property.
     */
    public AccountType getType() throws UnauthorizedOperationException {
        new ExternalAccountRight.Type().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return dao.getType();
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return dao;
    }

}
