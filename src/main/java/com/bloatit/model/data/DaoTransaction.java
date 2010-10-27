package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManger;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

@Entity
public class DaoTransaction extends DaoIdentifiable {

    @Column(updatable = false, nullable = false)
    private Date creationDate;
    @ManyToOne(optional=false)
    private DaoInternalAccount from;
    @ManyToOne(optional=false)
    private DaoAccount to;
    @Column(updatable = false, nullable = false)
    private BigDecimal amount;

    protected DaoTransaction() {
        super();
    }

    /**
     * Create a new transaction and update the two accounts.
     * 
     * @param from is the account from which we will take money.
     * @param to is the account where the money goes
     * @param amount is the quantity of money transfered.
     * @throws NotEnoughMoneyException 
     */
    public static DaoTransaction createAndPersist(DaoInternalAccount from, DaoAccount to, BigDecimal amount) throws NotEnoughMoneyException {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        DaoTransaction Transaction = new DaoTransaction(from, to, amount);
        try {
            session.save(Transaction);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return Transaction;

    }

    private DaoTransaction(DaoInternalAccount from, DaoAccount to, BigDecimal amount) throws NotEnoughMoneyException {
        super();
        // TODO Find how to manage this for the other account
        if(from.getAmount().compareTo(amount)<0){
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

    protected void setTo(DaoAccount to) {
        this.to = to;
    }

    protected void setFrom(DaoInternalAccount from) {
        this.from = from;
    }

}
