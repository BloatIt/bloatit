package com.bloatit.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

    @ManyToOne(optional = false)
    private DaoStringVersion currentVersion;

    @OneToMany(mappedBy = "versionedString", cascade = { CascadeType.ALL })
    @OrderBy(clause = "id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<DaoStringVersion> versions = new ArrayList<DaoStringVersion>();

    private DaoVersionedString(String content) {
        super();
        if (content == null) {
            throw new NonOptionalParameterException();
        }
        currentVersion = DaoStringVersion.createAndPersist(content, this);
        this.versions.add(currentVersion);
    }

    public static DaoVersionedString createAndPersist(String content) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoVersionedString version = new DaoVersionedString(content);
        try {
            session.save(version);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return version;
    }

    public void addVersion(String content) {
        this.currentVersion = DaoStringVersion.createAndPersist(content, this);
        versions.add(currentVersion);
    }

    public void changeCurrentVersion(DaoStringVersion version) {
        if (!version.getVersionedString().equals(this)) {
            throw new BadProgrammerException("VersionedString mismatch");
        }
        this.currentVersion = version;
    }

    public String getCurrentContent() {
        return currentVersion.getContent();
    }

    public PageIterable<DaoStringVersion> getVersions() {
        return new MappedList<DaoStringVersion>(versions);
    }

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
    protected DaoVersionedString() {
        super();
    }

    public DaoStringVersion getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(DaoStringVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void setVersions(List<DaoStringVersion> versions) {
        this.versions = versions;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DaoVersionedString other = (DaoVersionedString) obj;
        if (currentVersion == null) {
            if (other.currentVersion != null)
                return false;
        } else if (!currentVersion.equals(other.currentVersion))
            return false;
        if (versions == null) {
            if (other.versions != null)
                return false;
        } else if (!versions.equals(other.versions))
            return false;
        return true;
    }

}
