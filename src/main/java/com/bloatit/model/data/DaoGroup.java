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
import com.bloatit.model.data.util.SessionManager;

// DaoGroup is SQL keyword
@Entity
public class DaoGroup extends DaoActor {

    public enum Right {
        PUBLIC, PROTECTED;
    }

    public enum MemberStatus {
        UNKNOWN, IN_GROUP, ADMIN
    }

    // right is a SQL keyword.
    @Basic(optional = false)
    @Column(name = "group_right")
    private Right right;

    @OneToMany(mappedBy = "group")
    @Cascade(value = { CascadeType.ALL})
    private Set<DaoGroupMembership> groupMembership = new HashSet<DaoGroupMembership>(0);

    protected DaoGroup() {
        super();
    }

    /**
     * Create a group and add it into the db.
     * 
     * @param name
     *        it the unique and non updatable name of the group.
     * @param owner
     *        is the DaoMember creating this group.
     * @param right
     *        is the type of group we are creating.
     * @return the newly created group.
     * @throws HibernateException
     */
    static public DaoGroup createAndPersiste(String login, String email, Right right) throws HibernateException {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoGroup Group = new DaoGroup(login, email, right);
        try {
            session.save(Group);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return Group;
    }

    // TODO test me correctly !
    public static DaoGroup getByName(String name) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.createQuery("from com.bloatit.model.data.DaoGroup where login = :login");
        q.setString("login", name);
        return (DaoGroup) q.uniqueResult();
    }

    public DaoGroup(String login, String email, Right right) {
        super(login, email);
        this.right = right;
    }

    public PageIterable<DaoMember> getMembers() {

        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        Query filter = session.createFilter(getGroupMembership(), "select this.member order by login");
        final Query count = session.createFilter(getGroupMembership(), "select count(*)");
        return new QueryCollection<DaoMember>(filter, count);
    }

    public void addMember(DaoMember Member, boolean isAdmin) {
        groupMembership.add(new DaoGroupMembership(Member, this, isAdmin));
    }

    public void removeMember(DaoMember Member) {
        final DaoGroupMembership link = DaoGroupMembership.get(this, Member);
        groupMembership.remove(link);
        Member.getGroupMembership().remove(link);
        SessionManager.getSessionFactory().getCurrentSession().delete(link);
    }

    public Right getRight() {
        return right;
    }

    public void setRight(Right right) {
        this.right = right;
    }

    public MemberStatus getMemberStatus(DaoMember member) {
        Query q = SessionManager
                .getSessionFactory()
                .getCurrentSession()
                .createQuery("select gm from com.bloatit.model.data.DaoGroup g join g.groupMembership as gm join gm.member as m where g = :group and m = :member");
        q.setEntity("member", member);
        q.setEntity("group", this);
        DaoGroupMembership gm = (DaoGroupMembership) q.uniqueResult();
        if (gm == null) {
            return MemberStatus.UNKNOWN;
        }
        if (gm.isAdmin()) {
            return MemberStatus.ADMIN;
        }
        return MemberStatus.IN_GROUP;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setGroupMembership(Set<DaoGroupMembership> GroupMembership) {
        groupMembership = GroupMembership;
    }

    protected Set<DaoGroupMembership> getGroupMembership() {
        return groupMembership;
    }

}
