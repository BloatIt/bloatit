package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.util.HibernateUtil;

/**
 * WARNING class not used yet. Everything is done using directly member.
 */
@Entity
@MappedSuperclass
public class GroupMembership extends Identifiable {

	@OneToOne(optional = false)
	private Member member;

	// TODO find why I cannot make this parameter optional
	@OneToOne
	private Group group;

	@Basic(optional = false)
	private boolean isAdmin; // Should be Role enum

	protected GroupMembership() {
		super();
	}

	public static GroupMembership createAndPersiste(Member member, Group group, boolean isAdmin) throws HibernateException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		GroupMembership groupMembership = new GroupMembership(member, group, isAdmin);
		try {
			session.save(groupMembership);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return groupMembership;
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
