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

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

@Entity
public final class DaoBankTransaction extends DaoIdentifiable {

    public enum State {
        PENDING, ACCEPTED, REFUSED
    }

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    @Basic(optional = false)
    @Column(unique = true, updatable = false)
    private String token;
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
     * @param author
     * @return
     */
    public static PageIterable<DaoBankTransaction> getbankTransaction(DaoActor author) {
        return new QueryCollection<DaoBankTransaction>("from DaoBankTransaction where author = :author").setEntity("author", author);
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
        if (message == null || token == null || author == null || value == null || orderReference == null || message.isEmpty() || token.isEmpty()
                || orderReference.isEmpty()) {
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

}
