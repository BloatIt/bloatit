package com.bloatit.model.data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.model.util.HibernateUtil;

// member is a SQL keyword (in some specific implementations)
@Entity(name = "bloatit_member")
@NamedQuery(name = "getGroups", query = "select g from com.bloatit.model.data.Member m " 
	    + "join m.groupMembership as gm " + "join gm.group as g "
        + "where m = :member")
public class Member extends Identifiable {

	@Basic(optional = false)
	@Column(unique = true, updatable = false)
	private String login;
	@Basic(optional = false)
	private String password;
	private String firstname;
	private String lastname;
	@Basic(optional = false)
	private String email;
	@Basic(optional = false)
	private Date dateJoin;
	
	// this property is for hibernate mapping. It should never be used.
	@OneToMany(mappedBy = "member", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Set<GroupMembership> groupMembership = new HashSet<GroupMembership>(0);

	/**
	 * Create a member.
	 * 
	 * @param login
	 *            The login of the member.
	 * @param password
	 *            The password of the member (md5 ??)
	 * @return The newly created Member
	 * @throws HibernateException
	 *             if there is any problem connecting to the db. Then the
	 *             transaction is rollback, and a new one is begun.
	 */
	public static Member createAndPersist(String login, String password, String email) throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Member theMember = new Member(login, password, email);
		try {
			session.save(theMember);
		} catch (HibernateException e) {
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
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query q = session.createQuery("from com.bloatit.model.data.Member where login = :login");
		q.setString("login", login);
		return (Member) q.uniqueResult();
	}

	/**
	 * This method use a HQL request. If you intend to use "getByLogin", "exist"
	 * is useless. (In that case you'd better test if getByLogin != null, to
	 * minimize the number of HQL request).
	 */
	public static boolean exist(String login) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		// TODO use the count() in HQL
		Query q = session.createQuery("from com.bloatit.model.data.Member as m where login = :login");
		q.setString("login", login);
		return (q.uniqueResult() != null);
	}

	// TODO return a const list
	public List<Group> getGroups() {
		return getGroups(0, 0);
	}

	/**
	 * @param from
	 *            index of the first result?
	 * @param number
	 *            is the number max of results that the getGroup will return.
	 * @return the list of groups that this member is in.
	 */
	// TODO return a const list
	@SuppressWarnings("unchecked")
	public List<Group> getGroups(int from, int number) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getGroups");
		q.setParameter("member", this);
		q.setFirstResult(from);
		if (number != 0) {
			q.setFetchSize(number);
		}
		return q.list();
	}

	public void addGroup(Group aGroup, boolean isAdmin) {
		GroupMembership.createAndPersiste(this, aGroup, isAdmin);
	}

	protected Member() {
		super();
	}

	protected Member(String login, String password, String email) {
		super();
		this.login = login;
		this.password = password;
		this.email = email;
		dateJoin = new Date();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public Date getDateJoin() {
		return dateJoin;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setLogin(String login) {
		this.login = login;
	}

	protected void setDateJoin(Date dateJoin) {
		this.dateJoin = dateJoin;
	}

	protected void setGroupMembership(Set<GroupMembership> groupMembership) {
		this.groupMembership = groupMembership;
	}

	protected Set<GroupMembership> getGroupMembership() {
		return groupMembership;
	}
}
