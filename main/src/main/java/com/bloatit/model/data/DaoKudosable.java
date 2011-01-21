package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.bloatit.model.data.util.SessionManager;

/**
 * This represent a content that is Kudosable. There is no table DaoKudosable. Each
 * attribute is mapped by children classes.
 */
@MappedSuperclass
@Indexed
public abstract class DaoKudosable extends DaoUserContent {

    /**
     * This is the state of a Kudosable content. PENDING means that there is not enough
     * kudos to take a decision. VALIDATE means that the popularity is high enough to
     * validate this content. REFUSED means that the popularity is low enough to
     * delete/reject this content. HIDDEN is a state between pending and rejected.
     * Do not change the order !
     */
    public enum State {
        VALIDATED, PENDING, HIDDEN, REJECTED,
    }

    /**
     * The popularity is the sum of each value attached to each kudos that applies on this
     * kudosable. it is a cached value (It could be calculated)
     */
    @Basic(optional = false)
    @Field(store = Store.NO)
    private int popularity;

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    private final Set<DaoKudos> kudos = new HashSet<DaoKudos>(0);

    @Basic(optional = false)
    @Field(store = Store.NO)
    @Enumerated
    private State state;

    /**
     * initial state is PENDING, and popularity is 0.
     *
     * @param member the author.
     * @see DaoUserContent#DaoUserContent(DaoMember)
     */
    public DaoKudosable(final DaoMember member) {
        super(member);
        popularity = 0;
        setState(State.PENDING);
    }

    /**
     * Create a new DaoKudos and add it to the list of kudos.
     *
     * @return the new popularity
     */
    public final int addKudos(final DaoMember member, final int value) {
        final DaoKudos ku = DaoKudos.createAndPersist(member, value);
        kudos.add(ku);
        popularity += value;
        return popularity;
    }

    /**
     * Use a HQL query to find if a member as already kudosed this kudosable.
     *
     * @param member The member that could have kudosed this kudosable. (Don't even think
     *        of passing a null member)
     * @return true if member has kudosed, false otherwise.
     */
    public final boolean hasKudosed(final DaoMember member) {
        final Query q = SessionManager.getSessionFactory().getCurrentSession()
                .createQuery("select count(k) from " + this.getClass().getName() + " as a join a.kudos as k where k.member = :member and a = :this");
        q.setEntity("member", member);
        q.setEntity("this", this);
        return (Long) q.uniqueResult() > 0;
    }

    public final State getState() {
        return state;
    }

    public final int getPopularity() {
        return popularity;
    }

    /**
     * The state must be update from the framework layer.
     */
    public final void setState(final State state) {
        this.state = state;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoKudosable() {
        super();
    }
}
