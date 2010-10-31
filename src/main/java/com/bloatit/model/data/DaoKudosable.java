package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.model.data.util.SessionManager;

@MappedSuperclass
public abstract class DaoKudosable extends DaoUserContent {

    public enum State {
        PENDING, VALIDATED, REJECTED, HIDDEN
    }

    @Basic(optional = false)
    private int popularity;
    @OneToMany
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Set<DaoKudos> kudos = new HashSet<DaoKudos>(0);
    @Basic(optional = false)
    private State state;

    protected DaoKudosable() {
        super();
        popularity = 0;
    }

    public DaoKudosable(DaoMember member) {
        super(member);
        popularity = 0;
        setState(State.PENDING);
    }

    /**
     * @return the new popularity
     */
    public int addKudos(DaoMember member, int value) {
        kudos.add(new DaoKudos(member, value));
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

    public int getPopularity() {
        return popularity;
    }

    public boolean hasKudosed(DaoMember member) {
        Query f = SessionManager.getSessionFactory().getCurrentSession().createFilter(kudos, "where this.author = :author");
        f.setEntity("author", getAuthor());
        return f.uniqueResult() != null;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setState(State state) {
        this.state = state;
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
