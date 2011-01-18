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
public final class DaoTransaction extends DaoIdentifiable {

    @Column(updatable = false, nullable = false)
    private Date creationDate;
    @ManyToOne(optional = false)
    private DaoInternalAccount from;
    @ManyToOne(optional = false)
    private DaoAccount to;
    @Column(updatable = false, nullable = false)
    private BigDecimal amount;

    public static DaoTransaction createAndPersist(final DaoInternalAccount from, final DaoAccount to, final BigDecimal amount)
            throws NotEnoughMoneyException {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoTransaction transaction = new DaoTransaction(from, to, amount);
        try {
            session.save(transaction);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return transaction;
    }

    /**
     * Create a new transaction and update the two accounts.
     * 
     * @param from is the account from which we will take money.
     * @param to is the account where the money goes
     * @param amount is the quantity of money transfered.
     * @throws NotEnoughMoneyException if there is not enough money to make the
     *         transaction
     * @throws FatalErrorException if to == from
     * @throws NullPointerException if any of the parameters = null
     */
    private DaoTransaction(final DaoInternalAccount from, final DaoAccount to, final BigDecimal amount) throws NotEnoughMoneyException {
        super();
        if (from.equals(to)) {
            throw new FatalErrorException("Cannot create a transaction on the same account.", null);
        }
        if (!from.hasEnoughMoney(amount) || !to.hasEnoughMoney(amount.negate())) {
            throw new NotEnoughMoneyException();
        }
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.creationDate = new Date();
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
        return (Date) creationDate.clone();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoTransaction() {
        super();
    }

}
