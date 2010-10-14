package com.bloatit.model.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Base class to use with Hibernate. (A persistent class do not need to inherit
 * from Identifiable)
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Identifiable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	protected void setId(int id) {
		this.id = id;
	}

	protected int getId() {
		return id;
	}
}
