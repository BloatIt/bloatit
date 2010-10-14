package com.bloatit.model.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

// member is a SQL keyword (in some specific implementations)
@Entity(name = "bloatit_member")
@MappedSuperclass
public class Member extends Identifiable {

	@Basic(optional = false)
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

	protected Member() {
		super();
	}

	public Member(String login, String password) {
		super();
		this.login = login;
		this.password = password;
		dateJoin = new Date();
	}

	/**
	 * Automatically add the group in the group -> member relationship
	 */
	public void addGroup(Group group) {
		// TODO veryfi there is no bug with hibernate.
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
