package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.bloatit.common.Log;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

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
    protected DaoInternalAccount(final DaoActor actor) {
        super(actor);
        blocked = BigDecimal.ZERO;
    }

    public BigDecimal getBlocked() {
        return blocked;
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

    protected DaoInternalAccount() {
        super();
    }

}
