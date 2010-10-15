package com.bloatit.model.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.util.HibernateUtil;

//Group is SQL keyword
@Entity(name = "bloatit_group")
@MappedSuperclass
public class Group extends UserContent {

	public enum Right {
		PUBLIC, PRIVATE, PROTECTED;
	}

	// right is also a SQL keyword.
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

	static public Group createAndPersiste(String name, Member owner, Right right) throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
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
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
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
		// TODO make this works
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		return null;

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
