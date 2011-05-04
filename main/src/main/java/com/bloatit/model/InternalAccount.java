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

import com.bloatit.data.DaoInternalAccount;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.model.right.RgtInternalAccount;

/**
 * An internal account is an account containing the money we store for a user.
 * There is one internal account for each user and only one. An internal account
 * can never have an amount under zero. An internal account can have some money
 * blocked. When you contribute on an feature, you do not spend the money
 * directly, but it is blocked and you cannot use it elsewhere.
 * 
 * @author tguyard
 */
public final class InternalAccount extends Account<DaoInternalAccount> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoInternalAccount, InternalAccount> {
        @SuppressWarnings("synthetic-access")
        @Override
        public InternalAccount doCreate(final DaoInternalAccount dao) {
            return new InternalAccount(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static InternalAccount create(final DaoInternalAccount dao) {
        return new MyCreator().create(dao);
    }

    private InternalAccount(final DaoInternalAccount dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return true if you can access the <code>Blocked</code> property.
     * @see #authenticate(AuthenticatedUserToken)
     */
    public boolean canAccessBlocked() {
        return canAccess(new RgtInternalAccount.Blocked(), Action.READ);
    }

    /**
     * Return the amount blocked into contribution on non finished feature.
     * 
     * @return a positive {@link BigDecimal}.
     * @throws UnauthorizedOperationException if you do not have the right to
     *             access the <code>Bloked</code> property.
     */
    public BigDecimal getBlocked() throws UnauthorizedOperationException {
        tryAccess(new RgtInternalAccount.Blocked(), Action.READ);
        return getDao().getBlocked();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
