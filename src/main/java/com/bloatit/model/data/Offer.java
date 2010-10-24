package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Offer extends Kudosable {

	@ManyToOne
	private Demand demand;
	@OneToOne
	private Translatable text;
	@Basic(optional = false)
	private Date dateExpire;

	protected Offer() {
		super();
	}

	public Offer(Member author, Demand demand, Translatable text, Date dateExpire) {
	    super(author);
		this.demand = demand;
		this.text = text;
		this.dateExpire = dateExpire;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public Demand getDemand() {
		return demand;
	}

	public Translatable getText() {
		return text;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================
	
	protected void setDemand(Demand demand) {
    	this.demand = demand;
    }

	protected void setText(Translatable text) {
    	this.text = text;
    }
}
