package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

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
        final Query q = session.createQuery("from com.bloatit.model.data.DaoGroupMembership as gm where gm.bloatitGroup = :bloatitGroup and gm.member = :member");
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
}
