package com.bloatit.model.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.util.HibernateUtil;

// member is a SQL keyword (in some specific implementations)
@Entity(name = "bloatit_member")
@MappedSuperclass
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

	@ManyToMany
	@JoinTable(name = "GroupMembership", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "member_id"))
	private Set<Group> groups = new HashSet<Group>(0);

	/**
	 * For now it is a simple creator. But it will automatically add the Member to the default group.
	 * @param login The login of the member.
	 * @param password The password of the member (md5 ??)
	 * @return The newly created Member
	 * @throws HibernateException if there is any problem connecting to the db.
	 */
	public static Member createAndPersiste(String login, String password, String email) throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Member theMember = new Member(login, password, email);
		try {
			session.save(theMember);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return theMember;
	}
	
	public static Member getMemberByLogin(String login){
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Query q = session.createQuery("from Member where login = :login");
			q.setString("login", login);
			return (Member) q.list().get(0);
		}catch (HibernateException e) {
			return null;
		}
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

	/**
	 * Automatically add the group in the group -> member relationship
	 */
	public void addGroup(Group group) {
		groups.add(group);
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

	public Set<Group> getGroups() {
		return groups;
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

	protected void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
}
