package com.bloatit.model.data;

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
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

/**
 * A DaoBankTransaction represent a transaction with a real bank. It keep some
 * informations on the transaction.
 */
@Entity
public final class DaoBankTransaction extends DaoIdentifiable {

    private static final int DEFAULT_STRING_LENGTH = 64;

    /**
     * Enumerate the different state a BankTranscation can be in. After being
     * <code>ACCEPTED</code> a transaction must be <code>VALIDATED</code>.
     */
    public enum State {
        PENDING, ACCEPTED, REFUSED, VALIDATED
    }

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

    /**
     * @return all the <code>DaoBankTransaction</code> created by <code>author</code>,
     *         order by <code>creationDate</code>, most recent first.
     */
    public static PageIterable<DaoBankTransaction> getAllTransactionsOf(DaoActor author) {
        return new QueryCollection<DaoBankTransaction>("from DaoBankTransaction where author = :author order by creationDate DESC")
                .setEntity("author", author);
    }

    /**
     * @return the <code>DaoBankTransaction</code> with this <code>token</code>. Return
     *         null if not found.
     */
    public static DaoBankTransaction getByToken(String token) {
        return (DaoBankTransaction) SessionManager.createQuery("from DaoBankTransaction where token = :token").setString("token", token)
                .uniqueResult();
    }

    public static DaoBankTransaction createAndPersist(String message, String token, DaoActor author, BigDecimal value, String orderReference) {
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
    private DaoBankTransaction(String message, String token, DaoActor author, BigDecimal value, String orderReference) {
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

    protected DaoBankTransaction() {
        super();
    }

    /**
     * Set the state to {@link State#ACCEPTED} if the current state is
     * {@link State#PENDING}. Reset the modification date.
     */
    public void setAccepted() {
        if (state == State.PENDING) {
            modificationDate = new Date();
            state = State.ACCEPTED;
        }
    }

    /**
     * Set the state to validated and create a {@link DaoTransaction} from the external to
     * the internal account.
     *
     * @return true if performed, false otherwise.
     */
    public boolean validated() {
        if (state != State.ACCEPTED) {
            return false;
        }
        modificationDate = new Date();
        try {
            DaoTransaction.createAndPersist(author.getInternalAccount(), author.getExternalAccount(), getValue().negate());
            state = State.VALIDATED;
            return true;
        } catch (NotEnoughMoneyException e) {
            Log.data().fatal("Error when trying to validate a bankTransaction.", e);
            return false;
        }
    }

    /**
     * Set the state to {@link State#REFUSED}.
     */
    public void setRefused() {
        if (state == State.PENDING) {
            modificationDate = new Date();
            state = State.REFUSED;
        }
    }

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

    public final Date getCreationDate() {
        return creationDate;
    }

    public final Date getModificationDate() {
        return modificationDate;
    }

    public String getReference() {
        return reference;
    }

    public void setProcessInformations(String processInformations) {
        modificationDate = new Date();
        this.processInformations = processInformations;
    }

    public String getProcessInformations() {
        return processInformations;
    }
}
