package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.data.util.SessionManger;

// member is a SQL keyword (in some specific implementations)
@Entity(name = "bloatit_member")
@NamedQuery(name = "getGroups", query = "select g from com.bloatit.model.data.Member m " + "join m.groupMembership as gm " + "join gm.group as g "
        + "where m = :member order by g.login")
public class Member extends Actor {

	private String firstname;
	@Basic(optional = false)
	private String password;
	@Basic(optional = true)
	private String lastName;

	// this property is for hibernate mapping.
	@OneToMany(mappedBy = "member")
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	private Set<GroupMembership> groupMembership = new HashSet<GroupMembership>(0);

	/**
	 * Create a member. The member login must be unique, and you cannot change
	 * it.
	 * 
	 * @param login
	 *            The login of the member.
	 * @param password
	 *            The password of the member (md5 ??)
	 * @return The newly created Member
	 * @throws HibernateException
	 *             If there is any problem connecting to the db. Or if the
	 *             member as a non unique login. If an exception is thrown then
	 *             the transaction is rolled back and reopened.
	 * 
	 */
	public static Member createAndPersist(String login, String password, String email) throws HibernateException {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Member theMember = new Member(login, password, email);
		try {
			session.save(theMember);
		} catch (HibernateException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			session.beginTransaction();
			throw e;
		}
		return theMember;
	}
	
	/**
	 * Find a Member using its login.
	 * 
	 * @param login
	 *            the member login.
	 * @return null if not found.
	 */
	public static Member getByLogin(String login) {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Query q = session.createQuery("from com.bloatit.model.data.Member where login = :login");
		q.setString("login", login);
		return (Member) q.uniqueResult();
	}

	protected Member() {
		super();
	}

	protected Member(String login, String password, String email) {
		super(login, email);
		this.password = password;
	}

	/**
	 * @param aGroup
	 *            the group in which this member is added.
	 * @param isAdmin
	 *            tell if the member is an admin of the group 'aGroup'
	 */
	public void addToGroup(Group aGroup, boolean isAdmin) {
		groupMembership.add(new GroupMembership(this, aGroup, isAdmin));
	}

	/**
	 * @param aGroup
	 *            the group from which this member is removed.
	 */
	public void removeFromGroup(Group aGroup) {
		GroupMembership link = GroupMembership.get(aGroup, this);
		groupMembership.remove(link);
		aGroup.getGroupMembership().remove(link);
	}

	public QueryCollection<Group> getGroups() {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getGroups");
		q.setParameter("member", this);
		return new QueryCollection<Group>(q);
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastname() {
		return lastName;
	}

	public void setLastname(String name) {
		this.lastName = name;
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
