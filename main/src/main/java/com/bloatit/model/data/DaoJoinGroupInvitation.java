package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManager;

/**
 * TODO test me more ! This represent an invitation to join a group.
 */
@Entity
public class DaoJoinGroupInvitation extends DaoIdentifiable {

    /**
     * The state of an invitation track its time line. (First PENDING, then
     * REFUSED or
     * ACCEPTED)
     */
    public enum State {
        ACCEPTED, REFUSED, PENDING
    }

    @ManyToOne(optional = false)
    private DaoMember sender;
    @ManyToOne(optional = false)
    private DaoMember receiver;
    @ManyToOne(optional = false)
    private DaoGroup group;
    @Basic(optional = false)
    @Enumerated
    private State state;

    public static DaoJoinGroupInvitation createAndPersist(final DaoMember sender, final DaoMember reciever, final DaoGroup group) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoJoinGroupInvitation joinDemand = new DaoJoinGroupInvitation(sender, reciever, group);
        try {
            session.save(joinDemand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return joinDemand;
    }

    /**
     * Create a new invitation. Set the state to PENDING.
     * 
     * @throws NullPointerException if any of the parameters are null.
     */
    private DaoJoinGroupInvitation(final DaoMember sender, final DaoMember reciever, final DaoGroup group) {
        super();
        if (sender == null || reciever == null || group == null) {
            throw new NullPointerException();
        }
        this.sender = sender;
        this.receiver = reciever;
        this.group = group;
        setState(State.PENDING);
    }

    /**
     * Set the state to accepted and add the receiver into the list of members
     * of
     * this.group. If the state is not PENDING then do nothing.
     */
    public void accept() {
        if (state == State.PENDING) {
            receiver.addToGroup(group, false);
            setState(State.ACCEPTED);
        }
    }

    /**
     * Set the state to refused. If the state is not PENDING then do nothing.
     */
    public void refuse() {
        if (state == State.PENDING) {
            setState(State.REFUSED);
        }
    }

    public DaoMember getSender() {
        return sender;
    }

    public DaoMember getReceiver() {
        return receiver;
    }

    public State getState() {
        return state;
    }

    public DaoGroup getGroup() {
        return group;
    }

    public static DaoJoinGroupInvitation getInvitation(final DaoGroup group, final DaoMember member) {
        return (DaoJoinGroupInvitation) SessionManager.createQuery("from DaoJoinGroupInvitation where group = :group and receiver = :member")
                .setEntity("group", group).setEntity("member", member).uniqueResult();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setSender(final DaoMember sender) {
        this.sender = sender;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setReceiver(final DaoMember reciever) {
        this.receiver = reciever;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setGroup(final DaoGroup group) {
        this.group = group;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoJoinGroupInvitation() {
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setState(final State state) {
        this.state = state;
    }
}
