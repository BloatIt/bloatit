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
import com.bloatit.model.right.Action;
import com.bloatit.model.right.ExternalAccountRight;
import com.bloatit.rest.resources.ModelClassVisitor;

/**
 * @see DaoExternalAccount
 */
public final class ExternalAccount extends Account<DaoExternalAccount> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    public ExternalAccount(final Actor<?> actor, final AccountType type, final String bankCode) {
        super(DaoExternalAccount.createAndPersist(actor.getDao(), type, bankCode));
    }

    private static final class MyCreator extends Creator<DaoExternalAccount, ExternalAccount> {
        @Override
        public ExternalAccount doCreate(final DaoExternalAccount dao) {
            return new ExternalAccount(dao);
        }
    }

    public static ExternalAccount create(final DaoExternalAccount dao) {
        return new MyCreator().create(dao);
    }

    ExternalAccount(final DaoExternalAccount dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return true if you can access the <code>BankCode</code> property.
     */
    public boolean canAccessBankCode() {
        return canAccess(new ExternalAccountRight.BankCode(), Action.READ);
    }

    /**
     * @return true if you can access the <code>Type</code> property.
     */
    public boolean canAccessType() {
        return canAccess(new ExternalAccountRight.Type(), Action.READ);
    }

    /**
     * @throws UnauthorizedOperationException if you do not have the right to
     *             access the <code>BankCode</code> property.
     */
    public String getBankCode() throws UnauthorizedOperationException {
        tryAccess(new ExternalAccountRight.BankCode(), Action.READ);
        return getDao().getBankCode();
    }

    /**
     * @throws UnauthorizedOperationException if you do not have the right to
     *             access the <code>Type</code> property.
     */
    public AccountType getType() throws UnauthorizedOperationException {
        tryAccess(new ExternalAccountRight.Type(), Action.READ);
        return getDao().getType();
    }
    
    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
