//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.common.Log;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * A DaoBankTransaction represent a transaction with a real bank. It keep some
 * informations on the transaction.
 */
@Entity
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "bankTransaction.byToken",
                           query = "from DaoBankTransaction where token = :token"),
                     }
             )
// @formatter:on
public class DaoBankTransaction extends DaoIdentifiable {

    /**
     * Enumerate the different state a BankTranscation can be in. After being
     * <code>AUTHORIZED</code> a transaction must be <code>AUTHORIZED</code>.
     */
    public enum State {
        /**
         * A <code>PENDING</code> BankTransaction is in an not known yet state.
         * We are waiting for more informations to know in which state it will
         * be.
         */
        PENDING,

        /**
         * A transaction is <code>AUTHORIZED</code> when you give the right card
         * code and the bank says that you can do the transaction.
         */
        AUTHORIZED,

        /**
         * A refused transaction is a transaction that went wrong (error in the
         * CB code, bug, not money in the account.
         */
        REFUSED,

        /**
         * When the transaction is actually performed it pass in
         * <code>VALIDATE</code> state.
         */
        VALIDATED
    }

    private static final int DEFAULT_STRING_LENGTH = 64;

    /**
     * When doing automatic transaction with a bank, we can received a message
     * (mostly error messages). Use this property to store them.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    /**
     * A token is used to identify a BankTransaction. It is unique for each
     * DaoBankTransaction. Most of the time the token is created by the Bank,
     * and the bank will ask for it during the process, to identify the
     * different requests. ( {@value #DEFAULT_STRING_LENGTH} length)
     */
    @Basic(optional = false)
    @Column(unique = true, updatable = false, length = DEFAULT_STRING_LENGTH)
    private String token;

    /**
     * This field is a spare data field. You can store whatever you want in it (
     * {@value #DEFAULT_STRING_LENGTH} length)
     */
    @Basic(optional = true)
    @Column(updatable = true, length = DEFAULT_STRING_LENGTH)
    private String processInformations;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DaoActor author;
    @Basic(optional = false)
    @Column(updatable = false)
    private Date creationDate;
    @Basic(optional = false)
    private Date modificationDate;
    @Column(nullable = false, updatable = false)
    private BigDecimal value;
    @Column(nullable = false, updatable = false)
    private BigDecimal valuePaid;
    @Enumerated
    @Column(nullable = false)
    private State state;
    /**
     * This is the order reference. It must be unique and it is often used by
     * the bank to make sure an order is not taken twice.
     */
    @Column(nullable = false, updatable = false, unique = true)
    private String reference;

    // ======================================================================
    // Static HQL queries.
    // ======================================================================

    /**
     * @return the <code>DaoBankTransaction</code> with this <code>token</code>.
     *         Return null if not found.
     */
    public static DaoBankTransaction getByToken(final String token) {
        return (DaoBankTransaction) SessionManager.getNamedQuery("bankTransaction.byToken").setString("token", token).uniqueResult();
    }

    // ======================================================================
    // Construction
    // ======================================================================

    public static DaoBankTransaction createAndPersist(final String message,
                                                      final String token,
                                                      final DaoActor author,
                                                      final BigDecimal value,
                                                      final BigDecimal valuePayed,
                                                      final String orderReference) {
        final DaoBankTransaction bankTransaction = new DaoBankTransaction(message, token, author, value, valuePayed, orderReference);
        try {
            SessionManager.getSessionFactory().getCurrentSession().save(bankTransaction);
        } catch (final HibernateException e) {
            SessionManager.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw e;
        }
        return bankTransaction;
    }

    /**
     * throw a {@link NonOptionalParameterException} if any of the parameters is
     * null (or string isEmpty).
     */
    private DaoBankTransaction(final String message,
                               final String token,
                               final DaoActor author,
                               final BigDecimal value,
                               final BigDecimal valuePayed,
                               final String orderReference) {
        super();
        if (message == null || token == null || author == null || value == null || valuePayed == null || orderReference == null) {
            throw new NonOptionalParameterException();
        }
        if (message.isEmpty() || token.isEmpty() || orderReference.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.message = message;
        this.token = token;
        this.author = author;
        this.value = value;
        this.valuePaid = valuePayed;
        this.state = State.PENDING;
        this.reference = orderReference;
        this.creationDate = new Date();
        this.modificationDate = (Date) this.creationDate.clone();
    }

    /**
     * Set the state to {@link State#AUTHORIZED} if the current state is
     * {@link State#PENDING}. Reset the modification date.
     */
    public void setAuthorized() {
        if (this.state == State.PENDING) {
            this.modificationDate = new Date();
            this.state = State.AUTHORIZED;
        }
    }
    /**
     * Set the state to validated and create a {@link DaoTransaction} from the
     * external to the internal account.
     * 
     * @return true if performed, false otherwise.
     */
    public boolean setValidated() {
        if (this.state != State.AUTHORIZED) {
            Log.data().fatal("Cannot VALIDATE bankTransaction: " + getId() + "; state should be AUTHORIZED but was: " + state);
            return false;
        }
        this.modificationDate = new Date();
        try {
            final DaoTransaction transaction = DaoTransaction.createAndPersist(this.author.getInternalAccount(),
                                                                               this.author.getExternalAccount(),
                                                                               getValue().negate());
            this.state = State.VALIDATED;
            Log.data().info("Banktransaction: " + getId() + " VALIDATED. transaction_id: " + transaction.getId());
            return true;
        } catch (final NotEnoughMoneyException e) {
            Log.data().fatal("Error when trying to validate a bankTransaction.", e);
            return false;
        }
    }

    /**
     * Set the state to {@link State#REFUSED}.
     */
    public void setRefused() {
        this.modificationDate = new Date();
        this.state = State.REFUSED;
    }

    public void setProcessInformations(final String processInformations) {
        this.modificationDate = new Date();
        this.processInformations = processInformations;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public String getMessage() {
        return this.message;
    }

    public String getToken() {
        return this.token;
    }

    public DaoActor getAuthor() {
        return this.author;
    }

    public BigDecimal getValuePaid() {
        return this.valuePaid;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public State getState() {
        return this.state;
    }

    /**
     * @return a clone of the creationDate
     */
    public Date getCreationDate() {
        return (Date) this.creationDate.clone();
    }

    /**
     * @return a clone of the creationDate
     */
    public Date getModificationDate() {
        return (Date) this.modificationDate.clone();
    }

    public String getReference() {
        return this.reference;
    }

    public String getProcessInformations() {
        return this.processInformations;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping.
    // ======================================================================

    protected DaoBankTransaction() {
        super();
    }

    // ======================================================================
    // equals and hashCode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.author == null) ? 0 : this.author.hashCode());
        result = prime * result + ((this.creationDate == null) ? 0 : this.creationDate.hashCode());
        result = prime * result + ((this.token == null) ? 0 : this.token.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DaoBankTransaction)) {
            return false;
        }
        final DaoBankTransaction other = (DaoBankTransaction) obj;
        if (this.author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!this.author.equals(other.author)) {
            return false;
        }
        if (this.creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!this.creationDate.equals(other.creationDate)) {
            return false;
        }
        if (this.token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!this.token.equals(other.token)) {
            return false;
        }
        return true;
    }
}
