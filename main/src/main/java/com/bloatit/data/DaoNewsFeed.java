package com.bloatit.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A message used to inform elveos' users of the current status.
 */
@Entity
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                          name = "newsFeed.getAll",
                          query = "FROM DaoNewsFeed WHERE isDeleted = :isDeleted ORDER BY creationDate DESC"),
                          
                        @NamedQuery(
                          name = "newsFeed.getAll.size",
                          query = "SELECT count(*) FROM DaoNewsFeed WHERE isDeleted = :isDeleted ORDER BY creationDate DESC"),
              })
//@formatter:on
public class DaoNewsFeed extends DaoIdentifiable {
    @Column(nullable = false, updatable = false, unique = false)
    private String message;

    @Basic(optional = false)
    @Column(updatable = false)
    private Date creationDate;

    @Basic(optional = false)
    @Field(store = Store.YES, index = Index.UN_TOKENIZED)
    private boolean isDeleted;

    // ======================================================================
    // Construction
    // ======================================================================

    public static DaoNewsFeed createAndPersist(final String message) {
        final DaoNewsFeed newsFeed = new DaoNewsFeed(message);
        try {
            SessionManager.getSessionFactory().getCurrentSession().save(newsFeed);
        } catch (final HibernateException e) {
            SessionManager.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw e;
        }
        return newsFeed;
    }

    private DaoNewsFeed(final String message) {
        super();
        if (message == null) {
            throw new NonOptionalParameterException("message cannot be null");
        }
        if (message.length() > 140) {
            throw new BadProgrammerException("Message length must be inferior to 140");
        }
        this.message = message;
        this.isDeleted = false;
        this.creationDate = new Date();
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public String getMessage() {
        return message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    // ======================================================================
    // Setters
    // ======================================================================

    public void delete() {
        if (isDeleted()) {
            throw new BadProgrammerException("Cannot deleted a message twice");
        }
        isDeleted = true;
    }

    public void restore() {
        if (!isDeleted()) {
            throw new BadProgrammerException("Cannot restore a non deleted message");
        }
        isDeleted = false;
    }

    // ======================================================================
    // Static accessors
    // ======================================================================

    public static PageIterable<DaoNewsFeed> getAll(final boolean isDeleted) {
        return new QueryCollection<DaoNewsFeed>("newsFeed.getAll").setBoolean("isDeleted", isDeleted);
    }

    // ======================================================================
    // Visitor
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao join team invitation.
     */
    protected DaoNewsFeed() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoNewsFeed other = (DaoNewsFeed) obj;
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        return true;
    }
}
