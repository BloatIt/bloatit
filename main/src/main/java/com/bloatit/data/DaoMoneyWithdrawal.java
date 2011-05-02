package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * A money withdrawal happens when a user withdraws money from his elveos
 * account.
 */
@Entity
public class DaoMoneyWithdrawal extends DaoIdentifiable {
    public enum State {
        /**
         * First state of a money withdrawal. Means user requested the money but
         * hasn't been treated by admins yet.
         */
        REQUESTED,

        /**
         * Request has been seen by admins, who placed the money transfer order,
         * but money transfer is not done yet
         */
        TREATED,

        /**
         * Money transfer has been completed successfully
         */
        COMPLETE,

        /**
         * User decided to cancel the Withdrawal (can only happen before
         * TREATED)
         */
        CANCELED,

        /**
         * The administrators decided to refuse the money transfer
         */
        REFUSED
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(value = { CascadeType.PERSIST })
    private DaoActor actor;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @Cascade(value = { CascadeType.ALL })
    private DaoTransaction transaction;

    @Column(nullable = false, updatable = false, unique = false)
    private String IBAN;

    @Column(nullable = false, updatable = false)
    private BigDecimal amountWithdrawn;

    @Basic(optional = false)
    @Column(updatable = false)
    private Date creationDate;

    /**
     * Stores the last date where a modification happened on the money
     * withdrawal.
     * <p>
     * Note: Updating comment does <b>NOT</b> updates this date.
     * </p>
     */
    @Basic(optional = false)
    private Date lastModificationDate;

    @Column(nullable = false, updatable = false, unique = true)
    private String reference;

    /**
     * Comments are meant for administrators to write some data on the
     * transaction
     */
    @Basic
    private String comment;

    @Enumerated
    @Column(nullable = false)
    private State state;

    /**
     * States why the transaction has been refused by administrators
     */
    @Basic
    private String refusalReason;

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Creates a new Money Withdrawal, with a default state of REQUESTED and a
     * null comment
     */
    public static DaoMoneyWithdrawal createAndPersist(final DaoActor actor,
                                                      final String IBAN,
                                                      final String reference,
                                                      final BigDecimal amountWithdrawn) {
        final DaoMoneyWithdrawal moneyWithdrawal = new DaoMoneyWithdrawal(actor, IBAN, amountWithdrawn, reference);
        try {
            SessionManager.getSessionFactory().getCurrentSession().save(moneyWithdrawal);
        } catch (final HibernateException e) {
            SessionManager.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw e;
        }
        return moneyWithdrawal;
    }

    private DaoMoneyWithdrawal(DaoActor actor, String IBAN, BigDecimal amountWithdrawn, String reference) {
        super();
        this.actor = actor;
        this.IBAN = IBAN;
        this.amountWithdrawn = amountWithdrawn;
        this.reference = reference;
        this.lastModificationDate = new Date();
        this.creationDate = new Date();
        this.state = State.REQUESTED;
    }

    // ======================================================================
    // Setters
    // ======================================================================

    public void setState(State newState) {
        state = newState;
        this.lastModificationDate = new Date();
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTransaction(DaoTransaction transaction) {
        this.transaction = transaction;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public DaoActor getActor() {
        return actor;
    }

    public DaoTransaction getTransaction() {
        return transaction;
    }

    public String getIBAN() {
        return IBAN;
    }

    public BigDecimal getAmountWithdrawn() {
        return amountWithdrawn;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public String getReference() {
        return reference;
    }

    public String getComment() {
        return comment;
    }

    public State getState() {
        return state;
    }

    public String getRefusalReason() {
        return refusalReason;
    }

    @Override
    public <ReturnType> ReturnType accept(DataClassVisitor<ReturnType> visitor) {
        return null;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao join team invitation.
     */
    protected DaoMoneyWithdrawal() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((IBAN == null) ? 0 : IBAN.hashCode());
        result = prime * result + ((actor == null) ? 0 : actor.hashCode());
        result = prime * result + ((amountWithdrawn == null) ? 0 : amountWithdrawn.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((reference == null) ? 0 : reference.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DaoMoneyWithdrawal other = (DaoMoneyWithdrawal) obj;
        if (IBAN == null) {
            if (other.IBAN != null)
                return false;
        } else if (!IBAN.equals(other.IBAN))
            return false;
        if (actor == null) {
            if (other.actor != null)
                return false;
        } else if (!actor.equals(other.actor))
            return false;
        if (amountWithdrawn == null) {
            if (other.amountWithdrawn != null)
                return false;
        } else if (!amountWithdrawn.equals(other.amountWithdrawn))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (reference == null) {
            if (other.reference != null)
                return false;
        } else if (!reference.equals(other.reference))
            return false;
        if (state != other.state)
            return false;
        return true;
    }
}
