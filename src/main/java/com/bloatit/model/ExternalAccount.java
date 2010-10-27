package com.bloatit.model;

import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoExternalAccount.AccountType;

public class ExternalAccount extends Account {

    private DaoExternalAccount dao;

    public ExternalAccount(DaoExternalAccount dao) {
        super();
        this.dao = dao;
    }

    protected DaoExternalAccount getDao() {
        return dao;
    }

    public String getBankCode() {
        return dao.getBankCode();
    }

    public AccountType getType() {
        return dao.getType();
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return null;
    }

}
