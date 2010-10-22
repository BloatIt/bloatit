package com.bloatit.model.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.data.util.SessionManger;

// Group is SQL keyword
@Entity(name = "bloatit_group")
@NamedQuery(name = "getMembers", query = "select m from com.bloatit.model.data.Group g join g.groupMembership as gm join gm.member as m where g = :group")
public class Group extends UserContent {

    public enum Right {
        PUBLIC, PRIVATE, PROTECTED;
    }

    // right is a SQL keyword.
    @Basic(optional = false)
    @Column(name = "group_right")
    private Right right;

    @Basic(optional = false)
    @Column(unique = true, updatable = false)
    private String name;
    private String logo;

    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> groupMembership = new HashSet<GroupMembership>(0);

    protected Group() {
        super();
    }

    /**
     * Create a group and add it into the db.
     * 
     * @param name it the unique and non updatable name of the group.
     * @param owner is the Member creating this group.
     * @param right is the type of group we are creating.
     * @return the newly created group.
     * @throws HibernateException
     */
    static public Group createAndPersiste(String name, Member owner, Right right) throws HibernateException {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Group group = new Group(name, owner, right);
        try {
            session.save(group);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return group;
    }

    public static Group getByName(String name) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Query q = session.createQuery("from com.bloatit.model.data.Group as g where g.name = :name");
        q.setString("name", name);
        if (q.list().size() == 1) {
            return (Group) q.list().get(0);
        }
        return null;
    }

    public Group(String name, Member owner, Right right) {
        super(owner);
        this.setName(name);
        this.right = right;
    }

    public List<Member> getMembers() {
        return getMembers(0, 0);
    }

    @SuppressWarnings("unchecked")
    public List<Member> getMembers(int from, int number) {
        Session session = SessionManger.getSessionFactory().getCurrentSession();
        Query q = session.getNamedQuery("getMembers");
        q.setParameter("group", this);
        q.setFirstResult(from);
        if (number != 0) {
            q.setFetchSize(number);
        }
        return q.list();
    }

    public String getName() {
        return name;
    }

    public Right getRight() {
        return right;
    }

    public void setRight(Right right) {
        this.right = right;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setName(String name) {
        this.name = name;
    }

    protected void setGroupMembership(Set<GroupMembership> groupMembership) {
        this.groupMembership = groupMembership;
    }

    protected Set<GroupMembership> getGroupMembership() {
        return groupMembership;
    }

}
