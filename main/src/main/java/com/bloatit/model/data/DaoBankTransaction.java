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

@Entity
public final class DaoBankTransaction extends DaoIdentifiable {

    private static final int DEFAULT_STRING_LENGTH = 64;

    public enum State {
        PENDING, ACCEPTED, REFUSED, VALIDATED
    }

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    @Basic(optional = false)
    @Column(unique = true, updatable = false, length = DEFAULT_STRING_LENGTH)
    private String token;

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
    @Column(nullable = false, updatable = false, unique = true)
    private String reference;

    /**
     * comment order by
     *
     * @param author
     * @return
     */
    public static PageIterable<DaoBankTransaction> getAllTransactionsOf(DaoActor author) {
        return new QueryCollection<DaoBankTransaction>("from DaoBankTransaction where author = :author").setEntity("author", author);
    }

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

    public void setAccepted() {
        if (state == State.PENDING) {
            modificationDate = new Date();
            state = State.ACCEPTED;
        }
    }

    /**
     * TODO comment Validate And create the transaction
     *
     * @return true if performed, false otherwise.
     */
    public boolean setValidated() {
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
