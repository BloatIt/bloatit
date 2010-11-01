package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

@Entity
public class DaoJoinGroupDemand extends DaoIdentifiable {
    enum State {
        ACCEPTED, REFUSED, PENDING
    }

    @ManyToOne(optional = false)
    private DaoMember sender;
    @ManyToOne(optional = false)
    private DaoMember reciever;
    @ManyToOne(optional = false)
    private DaoGroup group;
    @Basic(optional = false)
    @Enumerated
    private State state;

    public static DaoJoinGroupDemand createAndPersist(DaoMember sender, DaoMember reciever, DaoGroup group) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoJoinGroupDemand joinDemand = new DaoJoinGroupDemand(sender, reciever, group);
        try {
            session.save(joinDemand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return joinDemand;
    }

    private DaoJoinGroupDemand(DaoMember sender, DaoMember reciever, DaoGroup group) {
        super();
        this.sender = sender;
        this.reciever = reciever;
        this.group = group;
        this.setState(State.PENDING);
    }

    public void accept() {
        // TODO if pending ??
        reciever.addToGroup(group, false);
        setState(State.ACCEPTED);
    }

    public void refuse() {
        // TODO if pending ??
        setState(State.REFUSED);
    }

    public DaoMember getSender() {
        return sender;
    }

    public DaoMember getReciever() {
        return reciever;
    }

    public State getState() {
        return state;
    }

    public DaoGroup getGroup() {
        return group;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setSender(DaoMember sender) {
        this.sender = sender;
    }

    protected void setReciever(DaoMember reciever) {
        this.reciever = reciever;
    }

    protected void setGroup(DaoGroup group) {
        this.group = group;
    }

    protected DaoJoinGroupDemand() {}

    protected void setState(State state) {
        this.state = state;
    }

}
