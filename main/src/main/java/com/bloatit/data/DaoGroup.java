//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A group is an entity where people can be group...
 */
@Entity
public final class DaoGroup extends DaoActor {

    /**
     * There is 2 kinds of groups : The PUBLIC that everybody can see and and go
     * in. The PROTECTED that everybody can see, but require an invitation to go
     * in.
     */
    public enum Right {
        PUBLIC, PROTECTED;
    }

    /**
     * WARNING right is a SQL keyword. This is mapped as "group_right".
     */
    @Basic(optional = false)
    @Column(name = "group_right")
    private Right right;

    @Basic(optional = false)
    @Column(unique = false)
    private String contact;

    @Basic(optional = false)
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "bloatitGroup")
    @Cascade(value = { CascadeType.ALL })
    private final Set<DaoGroupMembership> groupMembership = new HashSet<DaoGroupMembership>(0);

    // ======================================================================
    // Static HQL Requests
    // ======================================================================

    public static DaoGroup getByName(final String name) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("from com.bloatit.data.DaoGroup where login = :login");
        q.setString("login", name);
        return (DaoGroup) q.uniqueResult();
    }

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Create a group and add it into the db.
     * 
     * @param login it the unique and non updatable name of the group.
     * @param email is the email to contact this group (Unique).
     * @param right is the type of group we are creating (Public or Private).
     * @return the newly created group.
     * @throws HibernateException
     */
    public static DaoGroup createAndPersiste(final String login, final String contact, final String description, final Right right) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoGroup group = new DaoGroup(login, contact, description, right);
        try {
            session.save(group);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return group;
    }

    /**
     * Create a DaoGroup
     * 
     * @param login is the name of the group. It must be unique.
     * @param contact ...
     * @param right is the default right value for this group.
     */
    private DaoGroup(final String login, final String contact, final String description, final Right right) {
        super(login);
        if (right == null || contact == null || contact.isEmpty() || description == null) {
            throw new NonOptionalParameterException();
        }
        this.right = right;
        this.contact = contact;
        this.description = description;
    }

    public void setRight(final Right right) {
        this.right = right;
    }

    @Override
    public void setContact(final String contact) {
        this.contact = contact;
    }

    /**
     * Add a member in this group.
     * 
     * @param member The member to add
     * @param isAdmin true if the member need to have the right to administer
     *            this group. (This may change if the number of role change !)
     */
    public void addMember(final DaoMember member, final boolean isAdmin) {
        groupMembership.add(new DaoGroupMembership(member, this));
    }

    /**
     * Remove a member from the group
     */
    public void removeMember(final DaoMember member) {
        final DaoGroupMembership link = DaoGroupMembership.get(this, member);
        groupMembership.remove(link);
        member.getGroupMembership().remove(link);
        SessionManager.getSessionFactory().getCurrentSession().delete(link);
    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * @return all the member in this group. (Use a HQL query).
     */
    public PageIterable<DaoMember> getMembers() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query filter = session.createFilter(getGroupMembership(), "select this.member order by login");
        final Query count = session.createFilter(getGroupMembership(), "select count(*)");
        return new QueryCollection<DaoMember>(filter, count);
    }

    public Right getRight() {
        return right;
    }

    /**
     * Finds if a member is in this group, and which is its status.
     * 
     * @return {@code null} if the member is not in this group, or a set
     *         otherwise. <br />
     *         Note, the returned set can be empty if the user is only a Member
     */
    public EnumSet<UserGroupRight> getMemberStatus(final DaoMember member) {
        final Query q = SessionManager.getSessionFactory()
                                      .getCurrentSession()
                                      .createQuery("select gm from com.bloatit.data.DaoGroup g join g.groupMembership as gm join gm.member as m where g = :group and m = :member");
        q.setEntity("member", member);
        q.setEntity("group", this);
        final DaoGroupMembership gm = (DaoGroupMembership) q.uniqueResult();
        if (gm == null) {
            return null;
        }
        EnumSet<UserGroupRight> rights = EnumSet.noneOf(UserGroupRight.class);
        for (DaoGroupRight groupRight : gm.getRights()) {
            rights.add(groupRight.getUserStatus());
        }
        return rights;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getContact() {
        return contact;
    }

    /**
     * Used in DaoMember.
     */
    protected Set<DaoGroupMembership> getGroupMembership() {
        return groupMembership;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoGroup() {
        super();
    }

}
