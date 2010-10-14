package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * WARNING class not used yet. Everything is done using directly member.
 */
@Entity
@MappedSuperclass
public class GroupMembership extends Identifiable {

	@ManyToOne
	private Member member;

	@ManyToOne
	private Group group;
	@Basic(optional = false)
	private boolean isAdmin; // Should be Role enum

	protected GroupMembership() {
		super();
	}

	public GroupMembership(Member member, Group group, boolean isAdmin) {
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
