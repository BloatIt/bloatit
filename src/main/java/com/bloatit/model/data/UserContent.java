package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@Entity
public abstract class UserContent extends Identifiable {

	@OneToOne(optional = false)
	private Member author;
	@Basic(optional = false)
	private Date creationDate;

	protected UserContent() {
		creationDate = new Date();
	}

	public UserContent(Member member) {
		super();
		author = member;
		creationDate = new Date();
	}

	/**
	 * No final beacause it is deprecate for hibernate. but you should
	 * considered me as final
	 */
	public Member getMember() {
		return author;
	}

	/**
	 * No final beacause it is deprecate for hibernate. but you should
	 * considered me as final
	 */
	public Date getCreationDate() {
		return creationDate;
	}

}
