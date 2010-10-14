package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@Entity
public abstract class Kudosable extends UserContent {

	@Basic(optional = true)
	private int popularity;
	@OneToMany(mappedBy="kudosable")
	private Set<Kudos> kudos = new HashSet<Kudos>(0);

	protected Kudosable() {
		super();
		popularity = 0;
	}

	public Kudosable(Member member) {
		super(member);
	}

	/**
	 * Trivial calculation of the popularity 
	 * 
	 * @return the new popularity
	 */
	public int addKudos(Member member, int value) {
		kudos.add(new Kudos(member, value));
		return popularity += value;
	}

}
