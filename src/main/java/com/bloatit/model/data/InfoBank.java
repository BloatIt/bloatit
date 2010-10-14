package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Entity
@MappedSuperclass
public class InfoBank extends Identifiable {
	@Basic(optional = false)
	private Date creationDate;
	@Basic(optional = false)
	private Date lastModificationDate;
	@Basic(optional = false)
	private String iban;

	protected InfoBank() {
		super();
	}

	public InfoBank(String iban) {
		super();
		creationDate = new Date();
		lastModificationDate = new Date();
		this.iban = iban;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public String getIban() {
		return iban;
	}

}
