package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

/**
 * An external Account is our vision of a "reel" bank account.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public final class DaoExternalAccount extends DaoAccount {

    /**
     * Ok for now there is only IBAN code but they may have other types.
     */
    public enum AccountType {
        IBAN, VIRTUAL
    }

    /**
     * This is for tests only there is no check here it is a simple string.
     */
    @Basic(optional = false)
    private String bankCode;
    @Basic(optional = false)
    @Enumerated
    private AccountType type;

    /**
     * Create and persiste a DaoExternalAccount
     *
     * @see DaoExternalAccount#DaoExternalAccount(DaoActor, AccountType, String)
     */
    public static DaoExternalAccount createAndPersist(final DaoActor actor, final AccountType type, final String bankCode) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoExternalAccount account = new DaoExternalAccount(actor, type, bankCode);
        try {
            session.save(account);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return account;
    }

    /**
     * Create a new External account.
     *
     * @param actor is the owner of the account
     * @param type is the account type
     * @param bankCode is the bank code (for now IBAN...) THERE IS NO CHECK HERE !!
     * @throws NonOptionalParameterException if any of the parameter is null
     * @throws anExceptionToDefine when we will check the validity of the IBAN we will
     *         have to throw an exception if its not valid.
     */
    private DaoExternalAccount(final DaoActor actor, final AccountType type, final String bankCode) {
        super(actor);
        if (type == null || bankCode == null || bankCode.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.type = type;
        this.bankCode = bankCode;
    }

    protected DaoExternalAccount(final DaoActor actor) {
        super(actor);
        this.type = AccountType.VIRTUAL;
        this.bankCode = "";
    }

    public String getBankCode() {
        return bankCode;
    }

    public AccountType getType() {
        return type;
    }

    public final void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public final void setType(AccountType type) {
        this.type = type;
    }

    /**
     * Return true all the time.
     */
    @Override
    protected boolean hasEnoughMoney(final BigDecimal amount) {
        return true;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoExternalAccount() {
        super();
    }


}
