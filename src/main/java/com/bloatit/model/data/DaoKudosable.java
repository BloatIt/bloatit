package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.bloatit.model.data.util.SessionManager;

@MappedSuperclass
public abstract class DaoKudosable extends DaoUserContent {

    public enum State {
        PENDING, VALIDATED, REJECTED, HIDDEN
    }

    @Basic(optional = false)
    @Field(index=Index.NO, store=Store.NO)
    private int popularity;
    @OneToMany
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    private Set<DaoKudos> kudos = new HashSet<DaoKudos>(0);
    @Basic(optional = false)
    @Field(index=Index.NO, store=Store.NO)
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
        final DaoKudos ku = DaoKudos.createAndPersist(member, value);
        kudos.add(ku);
        SessionManager.flush();

        return popularity += value;
    }

    public State getState() {
        return state;
    }

    public int getPopularity() {
        return popularity;
    }

    public boolean hasKudosed(DaoMember member) {
        // Query f = SessionManager.getSessionFactory().getCurrentSession().createFilter(kudos, "where author = :author");
        // f.setEntity("author", getAuthor());
        final Query q = SessionManager.getSessionFactory()
                                      .getCurrentSession()
                                      .createQuery("select count(k) from " + this.getClass().getName()
                                              + " as a join a.kudos as k where k.member = :member and a = :this");
        q.setEntity("member", member);
        q.setEntity("this", this);
        return (Long) q.uniqueResult() > 0;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    // ======================================================================
    // For hibernate mapping
    // ======================================================================


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
