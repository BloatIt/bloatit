package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
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
	
	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setCreationDate(Date creationDate) {
    	this.creationDate = creationDate;
    }

	protected void setLastModificationDate(Date lastModificationDate) {
    	this.lastModificationDate = lastModificationDate;
    }

	protected void setIban(String iban) {
    	this.iban = iban;
    }

}
