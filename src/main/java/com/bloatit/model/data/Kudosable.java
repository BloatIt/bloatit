package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass
public abstract class Kudosable extends UserContent {

    @Basic(optional = false)
    private int popularity;
    @OneToMany
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

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected int getPopularity() {
        return popularity;
    }

    protected void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    protected Set<Kudos> getKudos() {
        return kudos;
    }

    protected void setKudos(Set<Kudos> kudos) {
        this.kudos = kudos;
    }

}
