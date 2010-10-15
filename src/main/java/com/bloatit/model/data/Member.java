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

import com.bloatit.model.util.HibernateUtil;

// member is a SQL keyword (in some specific implementations)
@Entity(name = "bloatit_member")
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

	@OneToMany(mappedBy = "member",cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
	private Set<GroupMembership> groupMembership = new HashSet<GroupMembership>(0);

	/**
	 * For now it is a simple creator.
	 * 
	 * @param login
	 *            The login of the member.
	 * @param password
	 *            The password of the member (md5 ??)
	 * @return The newly created Member
	 * @throws HibernateException
	 *             if there is any problem connecting to the db.
	 */
	public static Member createAndPersiste(String login, String password, String email) throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Member theMember = new Member(login, password, email);
		try {
			session.save(theMember);
			theMember.addGroup(HibernateUtil.getEverybodyGroup(), false);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			session.beginTransaction();
			throw e;
		}
		return theMember;
	}

	public static Member getByLogin(String login) {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Query q = session.createQuery("from com.bloatit.model.data.Member where login = :login");
			q.setString("login", login);
			return (Member) q.uniqueResult();
	}
	
	public static boolean exist(String login){
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			// TODO use the count() in HQL
			Query q = session.createQuery("from com.bloatit.model.data.Member as m where login = :login");
			q.setString("login", login);
			return (q.uniqueResult() != null);
	}

	public List<Group> getGroups() {
		return getGroups(0, 0);
	}

	@SuppressWarnings("unchecked")
    public List<Group> getGroups(int from, int number) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query q = session.createQuery("select g from com.bloatit.model.data.Member m " 
				+ "join m.groupMembership as gm " 
				+ "join gm.group as g "
		        + "where m = :member");
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
