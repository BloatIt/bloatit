package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public abstract class UserContent extends Identifiable {

	@OneToOne
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
	 * No final because it is depreciated for hibernate. but you should
	 * considered me as final
	 */
	public Member getAuthor() {
		return author;
	}

	/**
	 * No final because it is depreciated for hibernate. but you should
	 * considered me as final
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setAuthor(Member author) {
		this.author = author;
	}

	protected void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
