package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;

import com.bloatit.common.FatalErrorException;
import com.bloatit.framework.right.MoneyRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.framework.right.RightManager.Role;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoInternalAccount;
import com.bloatit.model.data.DaoTransaction;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

public final class Transaction extends Identifiable {

    private final DaoTransaction dao;

    public Transaction(final DaoTransaction dao) {
        super();
        this.dao = dao;
    }

    Transaction(final InternalAccount from, final Account to, final BigDecimal amount) throws NotEnoughMoneyException {
        this.dao = DaoTransaction.createAndPersist(from.getDao(), to.getDaoAccount(), amount);
    }

    public boolean canAccessSomething() {
        return new MoneyRight.Everything().canAccess(calculateRole(), Action.READ);
    }

    public InternalAccount getFrom() {
        new MoneyRight.Everything().tryAccess(calculateRole(), Action.READ);
        return new InternalAccount(dao.getFrom());
    }

    public Account getTo() {
        new MoneyRight.Everything().tryAccess(calculateRole(), Action.READ);
        if (dao.getTo().getClass() == DaoInternalAccount.class) {
            return new InternalAccount((DaoInternalAccount) dao.getTo());
        } else if (dao.getTo().getClass() == DaoExternalAccount.class) {
            return new ExternalAccount((DaoExternalAccount) dao.getTo());
        }
        throw new FatalErrorException("Cannot find the right Account child class.", null);
    }

    public BigDecimal getAmount() {
        new MoneyRight.Everything().tryAccess(calculateRole(), Action.READ);
        return dao.getAmount();
    }

    public Date getCreationDate() {
        new MoneyRight.Everything().tryAccess(calculateRole(), Action.READ);
        return dao.getCreationDate();
    }

    @Override
    public int getId() {
        return dao.getId();
    }

    protected DaoTransaction getDao() {
        return dao;
    }

    protected EnumSet<Role> calculateRole() {
        if (getToken().getMember().getUnprotectedLogin().equals(dao.getFrom().getActor().getLogin())) {
            return calculateRole(dao.getFrom().getActor().getLogin());
        } else if (getToken().getMember().getUnprotectedLogin().equals(dao.getTo().getActor().getLogin())) {
            return calculateRole(dao.getTo().getActor().getLogin());
        } else {
            return EnumSet.of(Role.OTHER);
        }
    }
}
