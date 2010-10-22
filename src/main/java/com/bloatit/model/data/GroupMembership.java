package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManger;

/**
 * This class is for Hibernate only for now.
 */
@Entity
public class GroupMembership extends Identifiable {

	// TODO find why I cannot make this parameter non null
	@OneToOne
	private Member member;

	// TODO find why I cannot make this parameter non null
	@OneToOne
	private Group group;

	@Basic(optional = false)
	private boolean isAdmin; // Should be Role enum

	protected GroupMembership() {
		super();
	}

	public static GroupMembership createAndPersiste(Member member, Group group, boolean isAdmin) throws HibernateException {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		GroupMembership groupMembership = new GroupMembership(member, group, isAdmin);
		try {
			session.save(groupMembership);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return groupMembership;
	}
	
	public static GroupMembership Get(Group group, Member member){
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Query q = session.createQuery("from com.bloatit.model.data.GroupMembership as gm where gm.group = :group and gm.member = :member");
		q.setEntity("group", group);
		q.setEntity("member", member);
		return (GroupMembership) q.uniqueResult();
	}
	
	private GroupMembership(Member member, Group group, boolean isAdmin) {
		this.member = member;
		this.group = group;
		this.isAdmin = isAdmin;
	}
	
	public Member getMember() {
		return member;
	}

	public Group getGroup() {
		return group;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setMember(Member member) {
		this.member = member;
	}

	protected void setGroup(Group group) {
		this.group = group;
	}

	protected void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
