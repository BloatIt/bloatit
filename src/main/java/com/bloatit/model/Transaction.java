package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoInternalAccount;
import com.bloatit.model.data.DaoTransaction;

public class Transaction extends Identifiable {

    private DaoTransaction dao;

    public Transaction(DaoTransaction dao) {
        super();
        this.dao = dao;
    }

    public InternalAccount getFrom() {
        return new InternalAccount(dao.getFrom());
    }

    public Account getTo() {
        if (dao.getTo().getClass() == DaoInternalAccount.class) {
            return new InternalAccount((DaoInternalAccount) dao.getTo());
        } else if (dao.getTo().getClass() == DaoExternalAccount.class) {
            return new ExternalAccount((DaoExternalAccount) dao.getTo());
        }
        throw new FatalErrorException("Cannot find the right Account child class.", null);
    }

    public BigDecimal getAmount() {
        return dao.getAmount();
    }

    public Date getCreationDate() {
        return dao.getCreationDate();
    }

    @Override
    protected int getDaoId() {
        return dao.getId();
    }

    protected DaoTransaction getDao() {
        return dao;
    }

}
