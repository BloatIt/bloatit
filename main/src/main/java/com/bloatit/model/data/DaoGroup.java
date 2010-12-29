package com.bloatit.model.data;

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

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

/**
 * A group is an entity where people can be group...
 */
@Entity
public final class DaoGroup extends DaoActor {

    /**
     * There is 2 kinds of groups : The PUBLIC that everybody can see and and go in. The
     * PROTECTED that everybody can see, but require an invitation to go in.
     */
    public enum Right {
        PUBLIC, PROTECTED;
    }

    /**
     * This is the status of a member in a Group. For now there is only ADMIN for a
     * priviliged role, but we can imagine a lot of other things (for example a role for
     * people who can "speak as")
     */
    public enum MemberStatus {
        UNKNOWN, IN_GROUP, ADMIN
    }

    /**
     * WARNING right is a SQL keyword. This is mapped as "group_right".
     */
    @Basic(optional = false)
    @Column(name = "group_right")
    private Right right;

    @OneToMany(mappedBy = "group")
    @Cascade(value = { CascadeType.ALL })
    private Set<DaoGroupMembership> groupMembership = new HashSet<DaoGroupMembership>(0);

    /**
     * Create a group and add it into the db.
     * 
     * @param name it the unique and non updatable name of the group.
     * @param owner is the DaoMember creating this group.
     * @param right is the type of group we are creating.
     * @return the newly created group.
     * @throws HibernateException
     */
    public static DaoGroup createAndPersiste(final String login, final String email, final Right right) throws HibernateException {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoGroup group = new DaoGroup(login, email, right);
        try {
            session.save(group);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return group;
    }

    // TODO test me correctly !
    public static DaoGroup getByName(final String name) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("from com.bloatit.model.data.DaoGroup where login = :login");
        q.setString("login", name);
        return (DaoGroup) q.uniqueResult();
    }

    // TODO comment
    private DaoGroup(final String login, final String email, final Right right) {
        super(login, email);
        if (right == null) {
            throw new NonOptionalParameterException();
        }
        this.right = right;
    }

    /**
     * @return all the member in this group. (Use a HQL query).
     */
    public PageIterable<DaoMember> getMembers() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query filter = session.createFilter(getGroupMembership(), "select this.member order by login");
        final Query count = session.createFilter(getGroupMembership(), "select count(*)");
        return new QueryCollection<DaoMember>(filter, count);
    }

    /**
     * Add a member in this group.
     * 
     * @param Member The member to add
     * @param isAdmin true if the member need to have the right to administer this group.
     *        (This may change if the number of role change !)
     */
    public void addMember(final DaoMember Member, final boolean isAdmin) {
        groupMembership.add(new DaoGroupMembership(Member, this, isAdmin));
    }

    /**
     * Remove a member from the group
     */
    public void removeMember(final DaoMember Member) {
        final DaoGroupMembership link = DaoGroupMembership.get(this, Member);
        groupMembership.remove(link);
        Member.getGroupMembership().remove(link);
        SessionManager.getSessionFactory().getCurrentSession().delete(link);
    }

    public Right getRight() {
        return right;
    }

    public void setRight(final Right right) {
        this.right = right;
    }

    /**
     * Finds if a member is in this group, and which is its status.
     * 
     * @return {@value MemberStatus#UNKNOWN} if the member is not in this group.
     */
    public MemberStatus getMemberStatus(final DaoMember member) {
        final Query q = SessionManager
                .getSessionFactory()
                .getCurrentSession()
                .createQuery("select gm from com.bloatit.model.data.DaoGroup g join g.groupMembership as gm join gm.member as m where g = :group and m = :member");
        q.setEntity("member", member);
        q.setEntity("group", this);
        final DaoGroupMembership gm = (DaoGroupMembership) q.uniqueResult();
        if (gm == null) {
            return MemberStatus.UNKNOWN;
        }
        if (gm.isAdmin()) {
            return MemberStatus.ADMIN;
        }
        return MemberStatus.IN_GROUP;
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
