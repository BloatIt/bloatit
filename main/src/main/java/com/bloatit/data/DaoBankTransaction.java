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
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.common.Log;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;

/**
 * A DaoBankTransaction represent a transaction with a real bank. It keep some
 * informations on the transaction.
 */
@Entity
public final class DaoBankTransaction extends DaoIdentifiable {

    /**
     * Enumerate the different state a BankTranscation can be in. After being
     * <code>AUTHORIZED</code> a transaction must be <code>AUTHORIZED</code>.
     */
    public enum State {
        /**
         * A <code>PENDING</code> BankTransaction is in an not known yet state. We are
         * waiting for more informations to know in which state it will be.
         */
        PENDING,

        /**
         * A transaction is <code>AUTHORIZED</code> when you give the right card code and
         * the bank says that you can do the transaction.
         */
        AUTHORIZED,

        /**
         * A refused transaction is a transaction that went wrong (error in the CB code,
         * bug, not money in the account.
         */
        REFUSED,

        /**
         * When the transaction is actually performed it pass in <code>VALIDATE</code> state.
         */
        VALIDATED
    }

    private static final int DEFAULT_STRING_LENGTH = 64;

    /**
     * When doing automatic transaction with a bank, we can received a message (mostly
     * error messages). Use this property to store them.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    /**
     * A token is used to identify a BankTransaction. It is unique for each
     * DaoBankTransaction. Most of the time the token is created by the Bank, and the bank
     * will ask for it during the process, to identify the different requests. (
     * {@value #DEFAULT_STRING_LENGTH} length)
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

    @ManyToOne(optional = false)
    private DaoActor author;
    @Basic(optional = false)
    @Column(updatable = false)
    private Date creationDate;
    @Basic(optional = false)
    private Date modificationDate;
    @Column(nullable = false, updatable = false)
    private BigDecimal value;
    @Enumerated
    @Column(nullable = false)
    private State state;
    /**
     * This is the order reference. It must be unique and it is often used by the bank to
     * make sure an order is not taken twice.
     */
    @Column(nullable = false, updatable = false, unique = true)
    private String reference;

    // ======================================================================
    // Static HQL queries.
    // ======================================================================

    /**
     * @return the <code>DaoBankTransaction</code> with this <code>token</code>. Return
     *         null if not found.
     */
    public static DaoBankTransaction getByToken(final String token) {
        return (DaoBankTransaction) SessionManager.createQuery("from DaoBankTransaction where token = :token").setString("token", token)
                .uniqueResult();
    }

    // ======================================================================
    // Construction
    // ======================================================================

    public static DaoBankTransaction createAndPersist(final String message,
                                                      final String token,
                                                      final DaoActor author,
                                                      final BigDecimal value,
                                                      final String orderReference) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoBankTransaction bankTransaction = new DaoBankTransaction(message, token, author, value, orderReference);
        try {
            session.save(bankTransaction);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return bankTransaction;
    }

    /**
     * throw a {@link NonOptionalParameterException} if any of the parameters is null (or
     * string isEmpty).
     */
    private DaoBankTransaction(final String message, final String token, final DaoActor author, final BigDecimal value, final String orderReference) {
        super();
        if (message == null || token == null || author == null || value == null || orderReference == null) {
            throw new NonOptionalParameterException();
        }
        if (message.isEmpty() || token.isEmpty() || orderReference.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.message = message;
        this.token = token;
        this.author = author;
        this.value = value;
        this.state = State.PENDING;
        this.reference = orderReference;
        this.creationDate = new Date();
        this.modificationDate = (Date) creationDate.clone();
    }

    /**
     * Set the state to {@link State#AUTHORIZED} if the current state is
     * {@link State#PENDING}. Reset the modification date.
     */
    public void setAuthorized() {
        if (state == State.PENDING) {
            modificationDate = new Date();
            state = State.AUTHORIZED;
        }
    }

    /**
     * Set the state to validated and create a {@link DaoTransaction} from the external to
     * the internal account.
     *
     * @return true if performed, false otherwise.
     */
    public boolean setValidated() {
        if (state != State.AUTHORIZED) {
            return false;
        }
        modificationDate = new Date();
        try {
            DaoTransaction.createAndPersist(author.getInternalAccount(), author.getExternalAccount(), getValue().negate());
            state = State.VALIDATED;
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
        modificationDate = new Date();
        state = State.REFUSED;
    }

    public void setProcessInformations(final String processInformations) {
        modificationDate = new Date();
        this.processInformations = processInformations;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public DaoActor getAuthor() {
        return author;
    }

    public BigDecimal getValue() {
        return value;
    }

    public State getState() {
        return state;
    }

    /**
     * @return a clone of the creationDate
     */
    public Date getCreationDate() {
        return (Date) creationDate.clone();
    }

    /**
     * @return a clone of the creationDate
     */
    public Date getModificationDate() {
        return (Date) modificationDate.clone();
    }

    public String getReference() {
        return reference;
    }

    public String getProcessInformations() {
        return processInformations;
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
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
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
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        return true;
    }

}
