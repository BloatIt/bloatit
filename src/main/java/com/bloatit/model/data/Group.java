package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

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
	private String logo;

	@ManyToMany
	@JoinTable(name = "GroupMembership", joinColumns = @JoinColumn(name = "member_id"),
	           inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<Member> members = new HashSet<Member>(0);

	protected Group() {
		super();
	}

	public Group(Member author, Right right) {
		super(author);
		this.right = right;
	}

	/**
	 * Automatically add the group in the member -> group relationship
	 */
	public void addMember(Member member) {
		// TODO veryfi there is no bug with hibernate.
		members.add(member);
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

	public Set<Member> getMembers() {
		return members;
	}

}
