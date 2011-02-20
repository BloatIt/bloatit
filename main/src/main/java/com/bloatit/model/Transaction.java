//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.right.AccountRight;
import com.bloatit.model.right.Action;

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
        super(DaoTransaction.createAndPersist(from.getDao(), to.getDao(), amount));
    }

    public boolean canAccessSomething() {
        return canAccess(new AccountRight.Transaction(), Action.READ);
    }

    public InternalAccount getFrom() throws UnauthorizedOperationException {
        tryAccess(new AccountRight.Transaction(), Action.READ);
        return getFromUnprotected();
    }

    private InternalAccount getFromUnprotected() {
        return InternalAccount.create(getDao().getFrom());
    }

    private Account<?> getToUnprotected() {
        if (getDao().getTo().getClass() == DaoInternalAccount.class) {
            return InternalAccount.create((DaoInternalAccount) getDao().getTo());
        } else if (getDao().getTo().getClass() == DaoExternalAccount.class) {
            return ExternalAccount.create((DaoExternalAccount) getDao().getTo());
        }
        throw new FatalErrorException("Cannot find the right Account child class.", null);
    }

    public Account<?> getTo() throws UnauthorizedOperationException {
        tryAccess(new AccountRight.Transaction(), Action.READ);
        return getToUnprotected();
    }

    public BigDecimal getAmount() throws UnauthorizedOperationException {
        tryAccess(new AccountRight.Transaction(), Action.READ);
        return getDao().getAmount();
    }

    public Date getCreationDate() throws UnauthorizedOperationException {
        tryAccess(new AccountRight.Transaction(), Action.READ);
        return getDao().getCreationDate();
    }

    @Override
    protected boolean isMine(Member member) {
        return getFromUnprotected().isMine(member) || getToUnprotected().isMine(member);
    }
}
