package com.bloatit.framework;

import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoExternalAccount.AccountType;

public class ExternalAccount extends Account {

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

    public String getBankCode() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return dao.getBankCode();
    }

    public AccountType getType() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return dao.getType();
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return null;
    }

}
