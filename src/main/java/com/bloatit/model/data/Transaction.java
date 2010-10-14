package com.bloatit.model.data;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Entity
@MappedSuperclass
public class Transaction extends UserContent {

	@Basic(optional = false)
	private BigDecimal amount;
	
	// For hibernate mapping only:
	@ManyToOne
	private Demand demand;

	public Transaction() {
	}

	public Transaction(Member member, BigDecimal amount) {
		super(member);
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

}
