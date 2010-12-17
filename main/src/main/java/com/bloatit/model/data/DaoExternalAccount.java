package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

/**
 * An external Account is our vision of a "reel" bank account.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DaoExternalAccount extends DaoAccount {

    /**
     * Ok for now there is only IBAN code but they may have other types.
     */
    public enum AccountType {
        IBAN
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
    public static DaoExternalAccount createAndPersist(final DaoActor Actor, final AccountType type, final String bankCode) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoExternalAccount account = new DaoExternalAccount(Actor, type, bankCode);
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
     * @param Actor is the owner of the account
     * @param type is the account type
     * @param bankCode is the bank code (for now IBAN...) THERE IS NO CHECK HERE
     *        !!
     * @throws NullPointerException if any of the parameter is null
     * @throws anExceptionToDefine when we will check the validity of the IBAN
     *         we will
     *         have to throw an exception if its not valid.
     */
    // TODO verify the bank code validity
    private DaoExternalAccount(final DaoActor Actor, final AccountType type, final String bankCode) {
        super(Actor);
        if (type == null || bankCode == null) {
            throw new NullPointerException();
        }
        this.type = type;
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public AccountType getType() {
        return type;
    }
    
    /**
     * Return true all the time.
     */
    @Override
    protected boolean hasEnoughMoney(BigDecimal amount) {
        return true;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoExternalAccount() {
        super();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setBankCode(final String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setType(final AccountType type) {
        this.type = type;
    }

}
