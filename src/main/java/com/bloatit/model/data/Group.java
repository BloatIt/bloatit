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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.data.util.SessionManger;

// Group is SQL keyword
@Entity(name = "bloatit_group")
@NamedQuery(name = "getMembers", query = "select m from com.bloatit.model.data.Group g join g.groupMembership as gm join gm.member as m where g = :group")
public class Group extends Actor {

	public enum Right {
		PUBLIC, PRIVATE, PROTECTED;
	}

	// right is a SQL keyword.
	@Basic(optional = false)
	@Column(name = "group_right")
	private Right right;

	@OneToMany(mappedBy = "group")
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	private Set<GroupMembership> groupMembership = new HashSet<GroupMembership>(0);

	protected Group() {
		super();
	}

	/**
	 * Create a group and add it into the db.
	 * 
	 * @param name
	 *            it the unique and non updatable name of the group.
	 * @param owner
	 *            is the Member creating this group.
	 * @param right
	 *            is the type of group we are creating.
	 * @return the newly created group.
	 * @throws HibernateException
	 */
	static public Group createAndPersiste(String login, String email, Right right) throws HibernateException {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Group group = new Group(login, email, right);
		try {
			session.save(group);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return group;
	}
	
	/**
	 * Find a Group using its login.
	 * 
	 * @param name
	 *            the member login.
	 * @return null if not found.
	 */
	public static Group getByName(String name) {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Query q = session.createQuery("from com.bloatit.model.data.Group where login = :login");
		q.setString("login", name);
		return (Group) q.uniqueResult();
	}

	public Group(String login, String email, Right right) {
		super(login, email);
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

	public void addMember(Member member, boolean isAdmin) {
		groupMembership.add(new GroupMembership(member, this, isAdmin));
	}

	public void removeMember(Member member) {
		GroupMembership link = GroupMembership.get(this, member);
		groupMembership.remove(link);
		member.getGroupMembership().remove(link);
	}

	public Right getRight() {
		return right;
	}

	public void setRight(Right right) {
		this.right = right;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setGroupMembership(Set<GroupMembership> groupMembership) {
		this.groupMembership = groupMembership;
	}

	protected Set<GroupMembership> getGroupMembership() {
		return groupMembership;
	}

}
