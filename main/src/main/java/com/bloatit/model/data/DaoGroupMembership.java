package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

/**
 * This class is for Hibernate only.
 */
@Entity
class DaoGroupMembership extends DaoIdentifiable {

    /**
     * TODO : declare a composite ID !
     */
    @ManyToOne(optional = false)
    private DaoMember member;

    /**
     * TODO : declare a composite ID !
     */
    @ManyToOne(optional = false)
    private DaoGroup group;

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
        final Query q = session.createQuery("from com.bloatit.model.data.DaoGroupMembership as gm where gm.group = :group and gm.member = :member");
        q.setEntity("group", group);
        q.setEntity("member", member);
        return (DaoGroupMembership) q.uniqueResult();
    }

    protected DaoGroupMembership(final DaoMember member, final DaoGroup group, final boolean isAdmin) {
        setMember(member);
        setGroup(group);
        setAdmin(isAdmin);
    }

    protected final DaoMember getMember() {
        return member;
    }

    protected final DaoGroup getGroup() {
        return group;
    }

    protected final boolean isAdmin() {
        return isAdmin;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    private void setMember(final DaoMember member) {
        this.member = member;
    }

    private void setGroup(final DaoGroup Group) {
        group = Group;
    }

    private void setAdmin(final boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    protected DaoGroupMembership() {
        super();
    }
}
