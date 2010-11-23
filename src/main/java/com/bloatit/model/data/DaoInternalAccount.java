package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.bloatit.model.exceptions.NotEnoughMoneyException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DaoInternalAccount extends DaoAccount {

    /**
     * This is the amount that is currently used by contributions. (amount - blocked) is
     * the money directly available in this account.
     */
    @Basic(optional = false)
    private BigDecimal blocked;

    public DaoInternalAccount() {
        super();
    }

    // Used in Member constructor.
    protected DaoInternalAccount(DaoActor actor) {
        super(actor);
        blocked = new BigDecimal("0");
    }

    public BigDecimal getBlocked() {
        return blocked;
    }

    protected void block(BigDecimal blocked) throws NotEnoughMoneyException {
        if (blocked.compareTo(this.getAmount()) > 0) {
            throw new NotEnoughMoneyException();
        }
        resetModificationDate();
        this.blocked = this.blocked.add(blocked);
        substractToAmountValue(blocked);
    }

    protected void unBlock(BigDecimal blocked) {
        // TODO verify that an amount is blocked
        resetModificationDate();
        this.blocked = this.blocked.subtract(blocked);
        addToAmountValue(blocked);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setBlocked(BigDecimal blocked) {
        this.blocked = blocked;
    }

}
