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
package com.bloatit.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.bloatit.common.Log;
import com.bloatit.data.exceptions.NotEnoughMoneyException;

/**
 * This is an internal account that store the amount of money a member have given us.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public final class DaoInternalAccount extends DaoAccount {

    /**
     * This is the amount that is currently used by contributions. (amount - blocked) is
     * the money directly available in this account.
     */
    @Basic(optional = false)
    private BigDecimal blocked;

    // Used in Member constructor.
    DaoInternalAccount(final DaoActor actor) {
        super(actor);
        blocked = BigDecimal.ZERO;
    }

    /**
     * bloc an amount of money, and reset the modification date.
     *
     * @param blocked the amount we want to block
     * @throws NotEnoughMoneyException if there is not enough money to block. (nothing is
     *         done, modification date is unchanged)
     */
    protected void block(final BigDecimal blocked) throws NotEnoughMoneyException {
        if (blocked.compareTo(getAmount()) > 0) {
            Log.data().fatal("Cannot block " + blocked.toEngineeringString() + " on account " + getId() + ", Throwing NotEnougthMoneyEcception.");
            throw new NotEnoughMoneyException();
        }
        resetModificationDate();
        this.blocked = this.blocked.add(blocked);
        substractToAmountValue(blocked);
    }

    /**
     * unbloc an amount of money, and reset the modification date.
     *
     * @param blocked the amount of money we want to unblock.
     * @throws NotEnoughMoneyException if there is not enough money already bloken.
     *         (nothing is done, modification date is unchanged)
     */
    protected void unBlock(final BigDecimal blocked) throws NotEnoughMoneyException {
        if (blocked.compareTo(this.blocked) > 0) {
            Log.data().fatal("Cannot unblock " + blocked.toEngineeringString() + " on account " + getId() + ", Throwing NotEnougthMoneyEcception.");
            throw new NotEnoughMoneyException();
        }
        resetModificationDate();
        this.blocked = this.blocked.subtract(blocked);
        addToAmountValue(blocked);
    }

    @Override
    protected boolean hasEnoughMoney(final BigDecimal amount) {
        return getAmount().compareTo(amount) >= 0;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    public BigDecimal getBlocked() {
        return blocked;
    }

    protected DaoInternalAccount() {
        super();
    }

}
