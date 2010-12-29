package com.bloatit.framework;

import java.math.BigDecimal;

import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoContribution;
import com.bloatit.model.data.DaoContribution.State;
import com.bloatit.model.data.DaoUserContent;

public final class Contribution extends UserContent {

    private final DaoContribution dao;

    public Contribution(final DaoContribution dao) {
        super();
        this.dao = dao;
    }

    public void accept(final Offer Offer) {
        // TODO
    }

    public void cancel() {
        // TODO
    }

    public BigDecimal getAmount() {
        return dao.getAmount();
    }

    public State getState() {
        return dao.getState();
    }

    public boolean canGetTransaction() {
        return new MoneyRight.Everything().canAccess(calculateRole(this), Action.READ);
    }

    public Transaction getTransaction() {
        new MoneyRight.Everything().tryAccess(calculateRole(this), Action.READ);
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
