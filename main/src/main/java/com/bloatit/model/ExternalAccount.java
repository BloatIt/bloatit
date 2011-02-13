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

import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoExternalAccount.AccountType;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.right.ExternalAccountRight;
import com.bloatit.model.right.RightManager.Action;

/**
 * @see DaoExternalAccount
 */
public final class ExternalAccount extends Account<DaoExternalAccount> {

    public ExternalAccount(final Actor<?> actor, final AccountType type, final String bankCode) {
        super(DaoExternalAccount.createAndPersist(actor.getDao(), type, bankCode));
    }

    public static ExternalAccount create(final DaoExternalAccount dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoExternalAccount> created = CacheManager.get(dao);
            if (created == null) {
                return new ExternalAccount(dao);
            }
            return (ExternalAccount) created;
        }
        return null;
    }

    private ExternalAccount(final DaoExternalAccount dao) {
        super(dao);
    }

    /**
     * @return true if you can access the <code>BankCode</code> property.
     */
    public boolean canAccessBankCode() {
        return new ExternalAccountRight.BankCode().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @return true if you can access the <code>Type</code> property.
     */
    public boolean canAccessType() {
        return new ExternalAccountRight.Type().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * @throws UnauthorizedOperationException if you do not have the right to access the
     * <code>BankCode</code> property.
     */
    public String getBankCode() throws UnauthorizedOperationException {
        new ExternalAccountRight.BankCode().tryAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
        return getDao().getBankCode();
    }

    /**
     * @throws UnauthorizedOperationException if you do not have the right to access the
     * <code>Type</code> property.
     */
    public AccountType getType() throws UnauthorizedOperationException {
        new ExternalAccountRight.Type().tryAccess(calculateRole(getActorUnprotected().getLogin()), Action.READ);
        return getDao().getType();
    }

}
