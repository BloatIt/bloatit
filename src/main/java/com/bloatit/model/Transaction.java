package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

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
        // TODO use the factory !
        return null; // new InternalAccount(dao.getFrom());
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
