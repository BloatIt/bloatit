package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoInternalAccount;

public class InternalAccount extends Account {

    private DaoInternalAccount dao;

    public InternalAccount(DaoInternalAccount dao) {
        super();
        this.dao = dao;

    }

    public DaoInternalAccount getDao() {
        return dao;
    }

    public BigDecimal getBlocked() {
        return dao.getBlocked();
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return dao;
    }

}
