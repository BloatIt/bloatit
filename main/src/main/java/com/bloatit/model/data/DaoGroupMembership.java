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
public class DaoGroupMembership extends DaoIdentifiable {

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

    protected DaoGroupMembership(final DaoMember Member, final DaoGroup Group, final boolean isAdmin) {
        member = Member;
        group = Group;
        this.isAdmin = isAdmin;
    }

    protected DaoMember getMember() {
        return member;
    }

    protected DaoGroup getGroup() {
        return group;
    }

    protected boolean isAdmin() {
        return isAdmin;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoGroupMembership() {
        super();
    }

    protected void setMember(final DaoMember Member) {
        member = Member;
    }

    protected void setGroup(final DaoGroup Group) {
        group = Group;
    }

    protected void setAdmin(final boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
