package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.model.data.DaoContribution;
import com.bloatit.model.data.DaoContribution.State;
import com.bloatit.model.data.DaoUserContent;

public class Contribution extends UserContent {

    private DaoContribution dao;

    public Contribution(DaoContribution dao) {
        super();
        this.dao = dao;
    }

    public void accept(Offer Offer) {}

    public void cancel() {}

    public BigDecimal getAmount() {
        return dao.getAmount();
    }

    public State getState() {
        return dao.getState();
    }

    public Transaction getTransaction() {
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
