package com.bloatit.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This class is for Hibernate only.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "bloatitGroup_id" }) })
class DaoGroupMembership extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMember member;

    @ManyToOne(optional = false)
    private DaoGroup bloatitGroup;

    /**
     * This will change to the DaoGroup#MemberStatus
     */
    @Basic(optional = false)
    private boolean isAdmin; // Should be Role enum

    /**
     * Get a GroupMembership line using its composite key. (HQL request)
     */
    protected static DaoGroupMembership get(final DaoGroup group, final DaoMember member) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session
                .createQuery("from com.bloatit.data.DaoGroupMembership as gm where gm.bloatitGroup = :bloatitGroup and gm.member = :member");
        q.setEntity("bloatitGroup", group);
        q.setEntity("member", member);
        return (DaoGroupMembership) q.uniqueResult();
    }

    protected DaoGroupMembership(final DaoMember member, final DaoGroup group, final boolean isAdmin) {
        this.member = member;
        this.bloatitGroup = group;
        this.isAdmin = isAdmin;
    }

    protected final DaoMember getMember() {
        return member;
    }

    protected final DaoGroup getGroup() {
        return bloatitGroup;
    }

    protected final boolean isAdmin() {
        return isAdmin;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoGroupMembership() {
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
        result = prime * result + ((bloatitGroup == null) ? 0 : bloatitGroup.hashCode());
        result = prime * result + ((member == null) ? 0 : member.hashCode());
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
        if (!(obj instanceof DaoGroupMembership)) {
            return false;
        }
        final DaoGroupMembership other = (DaoGroupMembership) obj;
        if (bloatitGroup == null) {
            if (other.bloatitGroup != null) {
                return false;
            }
        } else if (!bloatitGroup.equals(other.bloatitGroup)) {
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
