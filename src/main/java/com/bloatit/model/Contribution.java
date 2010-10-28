package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
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
