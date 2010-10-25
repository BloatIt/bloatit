package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ExternalAccount extends Account {
    public enum AccountType {
        IBAN
    }

    @Basic(optional = false)
    private String bankCode;
    @Basic(optional = false)
    @Enumerated
    private AccountType type;

    public ExternalAccount() {
        super();
    }

    public ExternalAccount(Member member, AccountType type, String bankCode) {
        super(member);
        this.type = type;
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public AccountType getType() {
        return type;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    protected void setType(AccountType type) {
        this.type = type;
    }

}
