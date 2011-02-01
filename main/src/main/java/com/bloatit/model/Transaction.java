package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;

import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.right.AccountRight;
import com.bloatit.model.right.RightManager.Action;
import com.bloatit.model.right.RightManager.Role;

public final class Transaction extends Identifiable<DaoTransaction> {

    public static Transaction create(final DaoTransaction dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoTransaction> created = CacheManager.get(dao);
            if (created == null) {
                return new Transaction(dao);
            }
            return (Transaction) created;
        }
        return null;
    }

    private Transaction(final DaoTransaction dao) {
        super(dao);
    }

    Transaction(final InternalAccount from, final Account<?> to, final BigDecimal amount) throws NotEnoughMoneyException {
        super(DaoTransaction.createAndPersist(from.getDao(), to.getDaoAccount(), amount));
    }

    public boolean canAccessSomething() {
        return new AccountRight.Transaction().canAccess(calculateRole(), Action.READ);
    }

    public InternalAccount getFrom() throws UnauthorizedOperationException {
        new AccountRight.Transaction().tryAccess(calculateRole(), Action.READ);
        return InternalAccount.create(dao.getFrom());
    }

    public Account<?> getTo() throws UnauthorizedOperationException {
        new AccountRight.Transaction().tryAccess(calculateRole(), Action.READ);
        if (dao.getTo().getClass() == DaoInternalAccount.class) {
            return InternalAccount.create((DaoInternalAccount) dao.getTo());
        } else if (dao.getTo().getClass() == DaoExternalAccount.class) {
            return ExternalAccount.create((DaoExternalAccount) dao.getTo());
        }
        throw new FatalErrorException("Cannot find the right Account child class.", null);
    }

    public BigDecimal getAmount() throws UnauthorizedOperationException {
        new AccountRight.Transaction().tryAccess(calculateRole(), Action.READ);
        return dao.getAmount();
    }

    public Date getCreationDate() throws UnauthorizedOperationException {
        new AccountRight.Transaction().tryAccess(calculateRole(), Action.READ);
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
        if (getAuthToken() == null) {
            return EnumSet.of(Role.NOBODY);
        }
        if (getAuthToken().getMember().getLoginUnprotected().equals(dao.getFrom().getActor().getLogin())) {
            return calculateRole(dao.getFrom().getActor().getLogin());
        } else if (getAuthToken().getMember().getLoginUnprotected().equals(dao.getTo().getActor().getLogin())) {
            return calculateRole(dao.getTo().getActor().getLogin());
        } else {
            return EnumSet.of(Role.OTHER);
        }
    }
}
