package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InternalAccount extends Account {

    /**
     * This is the amount that is currently used by contributions.
     * (amount - blocked) is the money directly available in this account.
     */
    @Basic(optional = false)
    private BigDecimal blocked;

    
    public InternalAccount() {
        super();
        // TODO Auto-generated constructor stub
    }

    public InternalAccount(Member member) {
        super(member);
        this.blocked = new BigDecimal("0");
    }
    
    public BigDecimal getBlocked() {
        return blocked;
    }
    
    protected void block(BigDecimal blocked){
        this.resetModificationDate();
        this.blocked = this.blocked.add(blocked);
        substractToAmountValue(blocked);
    }
    
    protected void unBlock(BigDecimal blocked){
        this.resetModificationDate();
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
