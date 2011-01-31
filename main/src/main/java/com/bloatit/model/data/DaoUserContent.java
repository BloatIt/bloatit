package com.bloatit.model.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.search.annotations.Indexed;

import com.bloatit.common.Log;
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

/**
 * A user content is a content created by a user. There is no table DaoUserContent (the
 * attributes are copied in the sub classes) A user content as an Author, and can be
 * posted in the name of a group. It also has a creation date.
 */
@MappedSuperclass
@Indexed
public abstract class DaoUserContent extends DaoIdentifiable {

    /**
     * This is the author of the user content.
     */
    @ManyToOne(optional = false)
    private DaoMember member;

    /**
     * Most of the time this is null. But when a user create a content in the name of a
     * group, asGroup point on it.
     */
    @ManyToOne(optional = true)
    private DaoGroup asGroup;

    @Basic(optional = false)
    private Date creationDate;

    @OneToMany(cascade = CascadeType.ALL)
    private final Set<DaoFileMetadata> files = new HashSet<DaoFileMetadata>();

    /**
     * Initialize the creation date to now.
     *
     * @param member is the author of this UserContent.
     * @throws NonOptionalParameterException if the member == null.
     */
    public DaoUserContent(final DaoMember member) {
        super();
        if (member == null) {
            Log.data().fatal("Cannot create a DaoUserContent with a null member.");
            throw new NonOptionalParameterException();
        }
        this.member = member;
        this.creationDate = new Date();
    }

    public final DaoMember getAuthor() {
        return member;
    }

    public final Date getCreationDate() {
        return (Date) creationDate.clone();
    }

    public final DaoGroup getAsGroup() {
        return asGroup;
    }

    /**
     * @return all the files associated with this DaoUserContent.
     */
    public final PageIterable<DaoFileMetadata> getFiles() {
        Session currentSession = SessionManager.getSessionFactory().getCurrentSession();
        Query filesQuery = currentSession.createFilter(files, "where relatedContent = :this").setEntity("this", this);
        Query filesSizeQuery = currentSession.createFilter(files, "select count(*) where relatedContent = :this").setEntity("this", this);
        return new QueryCollection<DaoFileMetadata>(filesQuery, filesSizeQuery);
    }

    /**
     * null is the default value and means that the content has a member as author.
     */
    public final void setAsGroup(final DaoGroup asGroup) {
        this.asGroup = asGroup;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoUserContent() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((asGroup == null) ? 0 : asGroup.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((member == null) ? 0 : member.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DaoUserContent)) {
            return false;
        }
        DaoUserContent other = (DaoUserContent) obj;
        if (asGroup == null) {
            if (other.asGroup != null) {
                return false;
            }
        } else if (!asGroup.equals(other.asGroup)) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (member == null) {
            if (other.member != null) {
                return false;
            }
        } else if (!member.equals(other.member)) {
            return false;
        }
        return true;
    }
}
