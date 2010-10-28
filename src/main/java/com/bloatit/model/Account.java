package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.TransactionList;
import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoMember;

public abstract class Account extends Identifiable {

    protected abstract DaoAccount getDaoAccount();

    public boolean canAccessSomething() {
        return new MoneyRight.Everything().canAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
    }

    public Date getLastModificationDate() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDaoAccount().getLastModificationDate();
    }

    public BigDecimal getAmount() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDaoAccount().getAmount();
    }

    public PageIterable<Transaction> getTransactions() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return new TransactionList(getDaoAccount().getTransactions());
    }

    public Actor getActor() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getActorUnprotected();
    }

    public Date getCreationDate() {
        new MoneyRight.Everything().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDaoAccount().getCreationDate();
    }

    protected Actor getActorUnprotected() {
        if (getDaoAccount().getActor().getClass() == DaoMember.class) {
            return new Member((DaoMember) getDaoAccount().getActor());
        } else if (getDaoAccount().getActor().getClass() == DaoGroup.class) {
            return new Group((DaoGroup) getDaoAccount().getActor());
        }
        throw new FatalErrorException("Cannot find the right Actor child class.", null);
    }

    @Override
    public int getId() {
        return getDaoAccount().getId();
    }

}
