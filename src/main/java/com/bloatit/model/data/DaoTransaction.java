package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A transaction is a transaction between an internal account and an other account.
 */
@Entity
public class DaoTransaction extends DaoIdentifiable {

    @Column(updatable = false, nullable = false)
    private Date creationDate;
    @ManyToOne(optional = false)
    private DaoInternalAccount from;
    @ManyToOne(optional = false)
    private DaoAccount to;
    @Column(updatable = false, nullable = false)
    private BigDecimal amount;

    public static DaoTransaction createAndPersist(DaoInternalAccount from, DaoAccount to, BigDecimal amount) throws NotEnoughMoneyException {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoTransaction Transaction = new DaoTransaction(from, to, amount);
        try {
            session.save(Transaction);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return Transaction;

    }

    /**
     * Create a new transaction and update the two accounts.
     * 
     * @param from is the account from which we will take money.
     * @param to is the account where the money goes
     * @param amount is the quantity of money transfered.
     * @throws NotEnoughMoneyException if there is not enough money to make the
     * transaction
     * @throws FatalErrorException if to == from
     * @throws NullPointerException if any of the parameters = null
     */
    private DaoTransaction(DaoInternalAccount from, DaoAccount to, BigDecimal amount) throws NotEnoughMoneyException {
        super();
        // TODO TEST ME MORE
        if (from == to) {
            throw new FatalErrorException("Cannot create a transaction on the same account.", null);
        }
        if (from.getAmount().compareTo(amount) < 0 || to.getAmount().compareTo(amount.negate()) < 0) {
            throw new NotEnoughMoneyException();
        }
        this.from = from;
        this.to = to;
        this.amount = amount;
        creationDate = new Date();
        from.substractToAmountValue(amount);
        to.addToAmountValue(amount);
    }

    public DaoInternalAccount getFrom() {
        return from;
    }

    public DaoAccount getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoTransaction() {
        super();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setTo(DaoAccount to) {
        this.to = to;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setFrom(DaoInternalAccount from) {
        this.from = from;
    }

}
