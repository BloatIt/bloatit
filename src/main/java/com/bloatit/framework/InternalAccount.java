package com.bloatit.framework;

import java.math.BigDecimal;

import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoInternalAccount;

public class InternalAccount extends Account {

    private final DaoInternalAccount dao;

    protected InternalAccount(DaoInternalAccount dao) {
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

    @Override
    protected DaoAccount getDaoAccount() {
        return dao;
    }

}
