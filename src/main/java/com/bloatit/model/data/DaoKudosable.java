package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass
public abstract class DaoKudosable extends DaoUserContent {

    public enum State {
        PENDING, VALIDATED, REJECTED, HIDDEN
    }

    @Basic(optional = false)
    private int popularity;
    @OneToMany
    private Set<DaoKudos> kudos = new HashSet<DaoKudos>(0);
    @Basic(optional = false)
    private State state;

    protected DaoKudosable() {
        super();
        popularity = 0;
    }

    public DaoKudosable(DaoActor Actor) {
        super(Actor);
        popularity = 0;
        setState(State.PENDING);
    }

    /**
     * Trivial calculation of the popularity
     * 
     * @return the new popularity
     */
    public int addKudos(DaoActor Actor, int value) {
        kudos.add(new DaoKudos(Actor, value));
        return popularity += value;
    }

    public State getState() {
        return state;
    }

    public void setValidated() {
        this.state = State.VALIDATED;
    }

    public void setRejected() {
        this.state = State.REJECTED;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setState(State state) {
        this.state = state;
    }

    protected int getPopularity() {
        return popularity;
    }

    protected void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    protected Set<DaoKudos> getKudos() {
        return kudos;
    }

    protected void setKudos(Set<DaoKudos> Kudos) {
        this.kudos = Kudos;
    }

}
