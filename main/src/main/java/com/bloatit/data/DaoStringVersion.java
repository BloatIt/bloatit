package com.bloatit.data;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoStringVersion extends DaoIdentifiable {

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private Date creationDate;

    @ManyToOne(optional = false, cascade = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoVersionedString versionedString;
    
    private DaoStringVersion(String content, DaoVersionedString versionedString) {
        super();
        if (content == null || versionedString == null) {
            throw new NonOptionalParameterException();
        }
        this.content = content;
        this.versionedString = versionedString;
        this.creationDate = new Date();
    }

    public static DaoStringVersion createAndPersist(String content, DaoVersionedString versionedString) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoStringVersion stringVersion = new DaoStringVersion(content, versionedString);
        try {
            session.save(stringVersion);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return stringVersion;
    }

    public final String getContent() {
        return content;
    }

    public final Date getCreationDate() {
        return creationDate;
    }

    public final DaoVersionedString getVersionedString() {
        return versionedString;
    }

    // public final void compact() {
    // TODO the compact algorithm.
    // }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao kudos.
     */
    protected DaoStringVersion() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
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
        DaoStringVersion other = (DaoStringVersion) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        return true;
    }

}
