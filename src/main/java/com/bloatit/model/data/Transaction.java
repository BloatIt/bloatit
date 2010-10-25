package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManger;

@Entity
public class Transaction extends Identifiable {

    @Column(updatable = false, nullable = false)
    private Date creationDate;
    @ManyToOne(optional=false)
    private InternalAccount from;
    @ManyToOne(optional=false)
    private Account to;
    @Column(updatable = false, nullable = false)
    private BigDecimal amount;

    protected Transaction() {
        super();
    }

    /**
     * Create a new transaction and update the two accounts.
     * 
     * @param from is the account from which we will take money.
     * @param to is the account where the money goes
     * @param amount is the quantity of money transfered.
     */
    public static Transaction createAndPersist(InternalAccount from, Account to, BigDecimal amount) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Transaction transaction = new Transaction(from, to, amount);
        try {
            session.save(transaction);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return transaction;

    }

    private Transaction(InternalAccount from, Account to, BigDecimal amount) {
        super();
        // TODO make sure the different accounts have enough money ...
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.creationDate = new Date();
        from.substractToAmountValue(amount);
        to.addToAmountValue(amount);
    }

    public InternalAccount getFrom() {
        return from;
    }

    public Account getTo() {
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

    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    protected void setTo(Account to) {
        this.to = to;
    }

    protected void setFrom(InternalAccount from) {
        this.from = from;
    }

}
