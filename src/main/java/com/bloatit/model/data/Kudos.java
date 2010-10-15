package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Kudos extends UserContent {

	@Basic(optional = false)
	private int value;

	public Kudos() {
	}

	public Kudos(Member member, int value) {
		super(member);
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setValue(int value) {
    	this.value = value;
    }
}
