//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * This represent an invitation to join a team.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "joinTeamInvitation.byTeamReceiver",
                           query = "FROM DaoJoinTeamInvitation WHERE team = :team AND receiver = :member"),
                       @NamedQuery(
                           name = "joinTeamInvitation.byTeamSender",
                           query = "FROM DaoJoinTeamInvitation WHERE team = :team AND sender = :member")
                       }
             )
// @formatter:on
public class DaoJoinTeamInvitation extends DaoIdentifiable {

    /**
     * <p>
     * The state of an invitation track its time line. (First PENDING, then
     * REFUSED or ACCEPTED)
     * </p>
     * <p>
     * The state DISCARDED means the invitation have been refused cause the user
     * accepted another invitation for the same team, canceling all other
     * invitations for this team. It is therefore an alternative to REFUSED or
     * ACCEPTED
     * </p>
     */
    public enum State {
        ACCEPTED, REFUSED, PENDING, DISCARDED
    }

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoMember sender;

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoMember receiver;

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoTeam team;

    @Basic(optional = false)
    @Enumerated
    private State state;

    // ======================================================================
    // Static HQL requests
    // ======================================================================

    /**
     * Gets the invitation to join a team.
     *
     * @param team the team this invitation is on.
     * @param member the member this invitation was sent to.
     * @return the invitation, are null if there is no invitation on that
     *         <code>team</code> sent to this <code>member</code>.
     */
    public static DaoJoinTeamInvitation getRecievedInvitation(final DaoTeam team, final DaoMember member) {
        return (DaoJoinTeamInvitation) SessionManager.getNamedQuery("joinTeamInvitation.byTeamReceiver")
                                                      .setEntity("team", team)
                                                      .setEntity("member", member)
                                                      .uniqueResult();
    }

    public static DaoJoinTeamInvitation getSentInvitation(final DaoTeam team, final DaoMember member) {
        return (DaoJoinTeamInvitation) SessionManager.getNamedQuery("joinTeamInvitation.byTeamSender")
                                                      .setEntity("team", team)
                                                      .setEntity("member", member)
                                                      .uniqueResult();
    }

    // ======================================================================
    // Construction.
    // ======================================================================

    public static DaoJoinTeamInvitation createAndPersist(final DaoMember sender, final DaoMember reciever, final DaoTeam team) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoJoinTeamInvitation joinInvitation = new DaoJoinTeamInvitation(sender, reciever, team);
        try {
            session.save(joinInvitation);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return joinInvitation;
    }

    /**
     * Create a new invitation. Set the state to PENDING.
     *
     * @throws NonOptionalParameterException if any of the parameters are null.
     */
    private DaoJoinTeamInvitation(final DaoMember sender, final DaoMember receiver, final DaoTeam team) {
        super();
        if (sender == null || receiver == null || team == null) {
            throw new NonOptionalParameterException();
        }
        this.sender = sender;
        this.receiver = receiver;
        this.team = team;
        this.state = State.PENDING;
    }

    /**
     * Set the state to accepted and add the receiver into the list of members
     * of this.team. If the state is not PENDING then do nothing.
     */
    public void accept() {
        if (this.state == State.PENDING) {
            this.receiver.addToTeam(this.team);
            this.state = State.ACCEPTED;
        }
    }

    /**
     * Set the state to refused. If the state is not PENDING then do nothing.
     */
    public void refuse() {
        if (this.state == State.PENDING) {
            this.state = State.REFUSED;
        }
    }

    /**
     * <p>
     * Sets the state to DISCARDED.
     * </p>
     * <p>
     * If the current state is not PENDING, nothing happens
     * </p>
     */
    public void discard() {
        if (this.state == State.PENDING) {
            this.state = State.DISCARDED;
        }
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // Getters
    // ======================================================================

    public DaoMember getSender() {
        return this.sender;
    }

    public DaoMember getReceiver() {
        return this.receiver;
    }

    public State getState() {
        return this.state;
    }

    public DaoTeam getTeam() {
        return this.team;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoJoinTeamInvitation() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.team == null) ? 0 : this.team.hashCode());
        result = prime * result + ((this.receiver == null) ? 0 : this.receiver.hashCode());
        result = prime * result + ((this.sender == null) ? 0 : this.sender.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DaoJoinTeamInvitation)) {
            return false;
        }
        final DaoJoinTeamInvitation other = (DaoJoinTeamInvitation) obj;
        if (this.team == null) {
            if (other.team != null) {
                return false;
            }
        } else if (!this.team.equals(other.team)) {
            return false;
        }
        if (this.receiver == null) {
            if (other.receiver != null) {
                return false;
            }
        } else if (!this.receiver.equals(other.receiver)) {
            return false;
        }
        if (this.sender == null) {
            if (other.sender != null) {
                return false;
            }
        } else if (!this.sender.equals(other.sender)) {
            return false;
        }
        return true;
    }

}
