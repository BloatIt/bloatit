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
import org.hibernate.annotations.NamedQuery;

import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

// DaoGroup is SQL keyword
@Entity
@NamedQuery(name = "getMembers", query = "select m from com.bloatit.model.data.DaoGroup g join g.groupMembership as gm join gm.member as m where g = :group")
public class DaoGroup extends DaoActor {

	public enum Right {
		PUBLIC, PRIVATE, PROTECTED;
	}

	// right is a SQL keyword.
	@Basic(optional = false)
	@Column(name = "group_right")
	private Right right;

	@OneToMany(mappedBy = "group")
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	private Set<DaoGroupMembership> groupMembership = new HashSet<DaoGroupMembership>(0);

	protected DaoGroup() {
		super();
	}

	/**
	 * Create a group and add it into the db.
	 * 
	 * @param name
	 *            it the unique and non updatable name of the group.
	 * @param owner
	 *            is the DaoMember creating this group.
	 * @param right
	 *            is the type of group we are creating.
	 * @return the newly created group.
	 * @throws HibernateException
	 */
	static public DaoGroup createAndPersiste(String login, String email, Right right) throws HibernateException {
		Session session = SessionManager.getSessionFactory().getCurrentSession();
		DaoGroup Group = new DaoGroup(login, email, right);
		try {
			session.save(Group);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return Group;
	}
	
	/**
	 * Find a DaoGroup using its login.
	 * 
	 * @param name
	 *            the member login.
	 * @return null if not found.
	 */
	public static DaoGroup getByName(String name) {
		Session session = SessionManager.getSessionFactory().getCurrentSession();
		Query q = session.createQuery("from com.bloatit.model.data.DaoGroup where login = :login");
		q.setString("login", name);
		return (DaoGroup) q.uniqueResult();
	}

	public DaoGroup(String login, String email, Right right) {
		super(login, email);
		this.right = right;
	}


	public PageIterable<DaoMember> getMembers() {
		Session session = SessionManager.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getMembers");
		q.setParameter("group", this);
		return new QueryCollection<DaoMember>(q);
	}

	public void addMember(DaoMember Member, boolean isAdmin) {
		groupMembership.add(new DaoGroupMembership(Member, this, isAdmin));
	}

	public void removeMember(DaoMember Member) {
		DaoGroupMembership link = DaoGroupMembership.get(this, Member);
		groupMembership.remove(link);
		Member.getGroupMembership().remove(link);
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

	protected void setGroupMembership(Set<DaoGroupMembership> GroupMembership) {
		this.groupMembership = GroupMembership;
	}

	protected Set<DaoGroupMembership> getGroupMembership() {
		return groupMembership;
	}

}
