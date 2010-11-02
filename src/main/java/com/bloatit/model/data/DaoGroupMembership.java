package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

/**
 * This class is for Hibernate only for now.
 */
@Entity
public class DaoGroupMembership extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMember member;

    @ManyToOne(optional = false)
    private DaoGroup group;

    @Basic(optional = false)
    private boolean isAdmin; // Should be Role enum

    protected DaoGroupMembership() {
        super();
    }

    public static DaoGroupMembership get(DaoGroup Group, DaoMember Member) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("from com.bloatit.model.data.DaoGroupMembership as gm where gm.group = :group and gm.member = :member");
        q.setEntity("group", Group);
        q.setEntity("member", Member);
        return (DaoGroupMembership) q.uniqueResult();
    }

    public DaoGroupMembership(DaoMember Member, DaoGroup Group, boolean isAdmin) {
        this.member = Member;
        this.group = Group;
        this.isAdmin = isAdmin;
    }

    public DaoMember getMember() {
        return member;
    }

    public DaoGroup getGroup() {
        return group;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setMember(DaoMember Member) {
        this.member = Member;
    }

    protected void setGroup(DaoGroup Group) {
        this.group = Group;
    }

    protected void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
