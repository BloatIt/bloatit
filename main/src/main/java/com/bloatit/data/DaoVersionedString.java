package com.bloatit.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang.NotImplementedException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public final class DaoVersionedString extends DaoIdentifiable {

    @OneToOne
    private DaoStringVersion currentVersion;

    @OneToMany(mappedBy = "versionedString", cascade = { CascadeType.ALL })
    @OrderBy(clause = "creationDate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private final List<DaoStringVersion> versions = new ArrayList<DaoStringVersion>();

    public static DaoVersionedString createAndPersist(final String content, final DaoMember author) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoVersionedString bug = new DaoVersionedString(content, author);
        try {
            session.save(bug);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return bug;
    }

    private DaoVersionedString(final String content, final DaoMember author) {
        super();
        if (content == null) {
            throw new NonOptionalParameterException();
        }
        currentVersion = new DaoStringVersion(content, this, author);
        this.versions.add(currentVersion);
    }

    public void addVersion(final String content, final DaoMember author) {
        this.currentVersion = new DaoStringVersion(content, this, author);
        versions.add(currentVersion);
    }

    public void useVersion(final DaoStringVersion version) {
        if (!version.getVersionedString().equals(this)) {
            throw new BadProgrammerException("VersionedString mismatch");
        }
        if (version.isCompacted()) {
            throw new NotImplementedException();
        }
        this.currentVersion = version;
    }

    public String getContent() {
        return currentVersion.getContent();
    }

    public PageIterable<DaoStringVersion> getVersions() {
        return new MappedList<DaoStringVersion>(versions);
    }

    public DaoStringVersion getCurrentVersion() {
        return currentVersion;
    }

    public void compact() {
        String previousContent = null;
        for (final DaoStringVersion version : versions) {
            if (!version.isCompacted()) {
                final String savedContent = version.getContent();
                if (previousContent != null) {
                    version.compact(previousContent);
                }
                previousContent = savedContent;
            } else {
                if (previousContent == null) {
                    throw new BadProgrammerException("First string version should never be compacted !");
                }
                previousContent = version.getContentUncompacted(previousContent);
            }
        }
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao kudos.
     */
    protected DaoVersionedString() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentVersion == null) ? 0 : currentVersion.hashCode());
        result = prime * result + ((versions == null) ? 0 : versions.hashCode());
        return result;
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

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
        final DaoVersionedString other = (DaoVersionedString) obj;
        if (currentVersion == null) {
            if (other.currentVersion != null) {
                return false;
            }
        } else if (!currentVersion.equals(other.currentVersion)) {
            return false;
        }
        if (versions == null) {
            if (other.versions != null) {
                return false;
            }
        } else if (!versions.equals(other.versions)) {
            return false;
        }
        return true;
    }

}
