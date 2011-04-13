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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;

import com.bloatit.common.Log;
import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.context.User.ActivationState;

/**
 * Ok if you need a comment to understand what is a member, then I cannot do
 * anything for you ...
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "member.byLogin",
                           query = "FROM DaoMember WHERE login = :login"),
                       @NamedQuery(
                           name = "member.byLoginPassword",
                           query = "FROM DaoMember WHERE login = :login AND password = :password"),


                       @NamedQuery(
                           name = "member.getReceivedInvitations.byStateTeam",
                           query = "FROM DaoJoinTeamInvitation " +
                                      "WHERE receiver = :receiver " +
                                      "AND state = :state  " +
                                      "AND team = :team"),
                          @NamedQuery(
                              name = "member.getReceivedInvitations.byStateTeam.size",
                              query =  "SELECT count(*)" +
                                          "FROM DaoJoinTeamInvitation " +
                                       "WHERE receiver = :receiver " +
                                       "AND state = :state  " +
                                       "AND team = :team"),

                       @NamedQuery(
                           name = "member.getReceivedInvitations.byState",
                           query = "FROM DaoJoinTeamInvitation " +
                                   "WHERE receiver = :receiver " +
                                   "AND state = :state "),
                       @NamedQuery(
                           name = "member.getReceivedInvitations.byState.size",
                           query = "SELECT count(*)" +
                                      "FROM DaoJoinTeamInvitation " +
                                   "WHERE receiver = :receiver " +
                                   "AND state = :state "),

                       @NamedQuery(
                           name = "member.getSentInvitations.byState",
                           query = "FROM DaoJoinTeamInvitation " +
                                   "WHERE sender = :sender " +
                                   "AND state = :state "),
                       @NamedQuery(
                           name = "member.getSentInvitations.byState.size",
                           query = "SELECT count(*)" +
                                   "FROM DaoJoinTeamInvitation " +
                                   "WHERE sender = :sender " +
                                   "AND state = :state "),

                       @NamedQuery(
                           name = "member.getSentInvitations.byStateTeam",
                           query = "SELECT count(*)" +
                                   "FROM DaoJoinTeamInvitation " +
                                   "WHERE sender = :sender " +
                                   "AND state = :state " +
                                   "AND team = :team"),
                       @NamedQuery(
                           name = "member.getSentInvitations.byStateTeam.size",
                           query = "SELECT count(*)" +
                                   "FROM DaoJoinTeamInvitation " +
                                   "WHERE sender = :sender " +
                                   "AND state = :state " +
                                   "AND team = :team"),

                       @NamedQuery(
                           name = "member.isInTeam",
                           query = "SELECT count(*) " +
                                   "FROM DaoMember m " +
                                   "JOIN m.teamMembership AS gm " +
                                   "JOIN gm.bloatitTeam AS g " +
                                   "WHERE m = :member AND g = :team"),


                      }
             )
// @formatter:on
public class DaoMember extends DaoActor {

    public enum Role {
        NORMAL, PRIVILEGED, REVIEWER, MODERATOR, ADMIN
    }

    private String fullname;

    @Basic(optional = false)
    private String password;
    @Basic(optional = false)
    private String salt;

    @Basic(optional = false)
    private Integer karma;

    @Basic(optional = false)
    @Enumerated
    private Role role;

    @Basic(optional = false)
    @Enumerated
    private ActivationState state;

    @Basic(optional = false)
    @Column(unique = true)
    private String email;

    @Basic(optional = false)
    private Locale locale;

    @ManyToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private DaoFileMetadata avatar;

    // this property is for hibernate mapping.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoTeamMembership> teamMembership = new ArrayList<DaoTeamMembership>(0);

    // ======================================================================
    // Static HQL requests
    // ======================================================================

    /**
     * Find a DaoMember using its login.
     *
     * @param login the member login.
     * @return null if not found. (or if login == null)
     */
    public static DaoMember getByLogin(final String login) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.getNamedQuery("member.byLogin");
        q.setString("login", login);
        return (DaoMember) q.uniqueResult();
    }

    /**
     * Find a DaoMember using its login, and password. This method can be use to
     * authenticate a use.
     *
     * @param login the member login.
     * @param password the password of the member "login". It is a string
     *            corresponding to the string in the database. This method does
     *            not perform any sha1 or md5 transformation.
     * @return null if not found. (or if login == null or password == null)
     */
    public static DaoMember getByLoginAndPassword(final String login, final String password) {
        if (login == null || password == null) {
            return null;
        }
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Criteria q = session.createCriteria(DaoMember.class)
                                  .add(Restrictions.like("login", login).ignoreCase())
                                  .add(Restrictions.like("password", password));

        return (DaoMember) q.uniqueResult();
    }

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Create a member. The member login must be unique, and you cannot change
     * it.
     *
     * @param login The login of the member.
     * @param password The password of the member (md5 ??)
     * @param locale the locale of the user.
     * @return The newly created DaoMember
     * @throws HibernateException If there is any problem connecting to the db.
     *             Or if the member as a non unique login. If an exception is
     *             thrown then the transaction is rolled back and reopened.
     */
    public static DaoMember createAndPersist(final String login, final String password, final String salt, final String email, final Locale locale) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoMember theMember = new DaoMember(login, password, salt, email, locale);
        try {
            session.save(theMember);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return theMember;
    }

    /**
     * You have to use CreateAndPersist instead of this constructor
     *
     * @param locale is the locale in which this user is. (The country and
     *            language.)
     * @see DaoMember#createAndPersist(String, String, String, Locale)
     */
    private DaoMember(final String login, final String password, final String salt, final String email, final Locale locale) {
        super(login);
        if (locale == null) {
            throw new NonOptionalParameterException("locale cannot be null!");
        }
        if (email == null) {
            throw new NonOptionalParameterException("email cannot be null!");
        }
        if (email.isEmpty()) {
            throw new NonOptionalParameterException("email cannot be empty!");
        }
        if (password == null) {
            throw new NonOptionalParameterException("Password cannot be null!");
        }
        if (password.isEmpty()) {
            throw new NonOptionalParameterException("Password cannot be empty!");
        }
        setLocale(locale);
        this.email = email;
        this.role = Role.NORMAL;
        this.state = ActivationState.VALIDATING;
        this.password = password;
        this.salt = salt;
        this.karma = 0;
        this.fullname = "";
    }

    /**
     * @param aTeam the team in which this member is added.
     */
    public void addToTeam(final DaoTeam aTeam) {
        final DaoTeamMembership daoTeamMembership = new DaoTeamMembership(this, aTeam);
        if (this.teamMembership.contains(daoTeamMembership)) {
            throw new BadProgrammerException("This member is already in the team: " + aTeam.getId());
        }
        this.teamMembership.add(daoTeamMembership);
        addTeamRight(aTeam, UserTeamRight.CONSULT);
    }

    /**
     * @param aTeam the team from which this member is removed.
     */
    public void removeFromTeam(final DaoTeam aTeam) {
        final DaoTeamMembership link = DaoTeamMembership.get(aTeam, this);
        if (link != null) {
            this.teamMembership.remove(link);
            aTeam.getTeamMembership().remove(link);
            SessionManager.getSessionFactory().getCurrentSession().delete(link);
        } else {
            Log.data().error("Try to remove a non existing DaoTeamMembership: team = " + aTeam.getId() + " member = " + getId());
        }
    }

    public void addTeamRight(final DaoTeam aTeam, final UserTeamRight newRight) {
        final DaoTeamMembership link = DaoTeamMembership.get(aTeam, this);
        if (link != null) {
            link.addUserRight(newRight);
        } else {
            Log.data().error("Trying to give user some rights in a team he doesn't belong: team = " + aTeam.getId() + " member = " + getId());
        }
    }

    public Set<UserTeamRight> getTeamRights(final DaoTeam aTeam) {
        return aTeam.getUserTeamRight(this);
    }

    public void removeTeamRight(final DaoTeam aTeam, final UserTeamRight removeRight) {
        final DaoTeamMembership link = DaoTeamMembership.get(aTeam, this);
        for (final DaoTeamRight dgr : link.getRights()) {
            if (dgr.getUserStatus().equals(removeRight)) {
                link.getRights().remove(dgr);
                SessionManager.getSessionFactory().getCurrentSession().delete(dgr);
                return;
            }
        }
        Log.data().error("Trying to remove user some rights in a team he doesn't belong: team = " + aTeam.getId() + " member = " + getId()
                + " right : " + removeRight);
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public void setActivationState(final ActivationState state) {
        this.state = state;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setFullname(final String firstname) {
        this.fullname = firstname;
    }

    @Override
    public void setContact(final String email) {
        this.email = email;
    }

    public void addToKarma(final int value) {
        this.karma += value;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Must be only used in update script. Salt should be a non updatable value
     * after that.
     *
     * @param salt the new salt.
     */
    void setSalt(final String salt) {
        this.salt = salt;
    }

    // ======================================================================
    // Getters
    // ======================================================================

    /**
     * [ Maybe it could be cool to have a parameter to list all the PUBLIC or
     * PROTECTED teams. ]
     *
     * @return All the teams this member is in. (Use a HQL query)
     */
    public PageIterable<DaoTeam> getTeams() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query filter = session.createFilter(getTeamMembership(), "select this.bloatitTeam order by login");
        final Query count = session.createFilter(getTeamMembership(), "select count(*)");
        return new QueryCollection<DaoTeam>(filter, count);
    }

    public Role getRole() {
        return this.role;
    }

    public ActivationState getActivationState() {
        return this.state;
    }

    public String getFullname() {
        return this.fullname;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

    public boolean passwordEquals(final String otherPassword) {
        return password.equals(otherPassword);
    }

    @Override
    public String getContact() {
        return this.email;
    }

    public Locale getLocale() {
        return this.locale;
    }

    /**
     * @return All the features created by this member.
     * @param asMemberOnly the result must contains only result that are not done as name of a team.
     */
    public PageIterable<DaoFeature> getFeatures(boolean asMemberOnly) {
        return getUserContent(DaoFeature.class, asMemberOnly);
    }

    /**
     * @return All the kudos created by this member.
     */
    public PageIterable<DaoKudos> getKudos() {
        return getUserContent(DaoKudos.class, false);
    }

    /**
     * @return All the contributions created by this member.
     * @param asMemberOnly the result must contains only result that are not done as name of a team.
     */
    public PageIterable<DaoContribution> getContributions(boolean asMemberOnly) {
        return getUserContent(DaoContribution.class , asMemberOnly);
    }

    /**
     * @return All the Comments created by this member.
     * @param asMemberOnly the result must contains only result that are not done as name of a team.
     */
    public PageIterable<DaoComment> getComments(boolean asMemberOnly) {
        return getUserContent(DaoComment.class, asMemberOnly);
    }

    /**
     * @return All the Offers created by this member.
     * @param asMemberOnly the result must contains only result that are not done as name of a team.
     */
    public PageIterable<DaoOffer> getOffers(boolean asMemberOnly) {
        return getUserContent(DaoOffer.class, asMemberOnly);
    }

    /**
     * @return All the Translations created by this member.
     * @param asMemberOnly the result must contains only result that are not done as name of a team.
     */
    public PageIterable<DaoTranslation> getTranslations(boolean asMemberOnly) {
        return getUserContent(DaoTranslation.class, asMemberOnly);
    }

    /**
     * @return All the received invitation to join a team which are in a
     *         specified state
     */
    public PageIterable<DaoJoinTeamInvitation> getReceivedInvitation(final State state) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getReceivedInvitations.byState").setEntity("receiver", this).setParameter("state",
                                                                                                                                            state);
    }

    /**
     * @param state the state of the invitation (ACCEPTED, PENDING, REFUSED)
     * @param team the team for which the invitations have been sent
     * @return All the received invitation to join a specific team, which are in
     *         a given state
     */
    public PageIterable<DaoJoinTeamInvitation> getReceivedInvitation(final State state, final DaoTeam team) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getReceivedInvitations.byStateTeam").setEntity("receiver", this)
                                                                                                      .setParameter("state", state)
                                                                                                      .setEntity("team", team);
    }

    public PageIterable<DaoJoinTeamInvitation> getSentInvitation(final State state, final DaoTeam team) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getSentInvitations.byStateTeam").setEntity("sender", this)
                                                                                                  .setParameter("state", state)
                                                                                                  .setEntity("team", team);
    }

    /**
     * @return All the sent invitation to join a team which are in a specified
     *         state
     */
    public PageIterable<DaoJoinTeamInvitation> getSentInvitation(final State state) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getSentInvitations.byState").setEntity("sender", this).setEntity("state", state);
    }

    /**
     * @return if the current member is in the "team".
     */
    public boolean isInTeam(final DaoTeam team) {
        final Query q = SessionManager.getNamedQuery("member.isInTeam");
        q.setEntity("member", this);
        q.setEntity("team", team);
        return ((Long) q.uniqueResult()) >= 1;
    }

    public Integer getKarma() {
        return this.karma;
    }

    /**
     * Base method to all the get something created by the user.
     * @param asMemberOnly the result must contains only result that are not done as name of a team.
     */
    private <T extends DaoUserContent> PageIterable<T> getUserContent(final Class<T> theClass, boolean asMemberOnly) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(theClass);
        final Query query = SessionManager.createQuery("from " + meta.getEntityName() + " as x where x.member = :author"+ (asMemberOnly?" AND x.asTeam = null":""));
        final Query size = SessionManager.createQuery("SELECT count(*) from " + meta.getEntityName() + " as x where x.member = :author"+ (asMemberOnly?" AND x.asTeam = null":""));
        final QueryCollection<T> q = new QueryCollection<T>(query, size);
        q.setEntity("author", this);
        return q;
    }

    /**
     * used by DaoTeam
     */
    protected List<DaoTeamMembership> getTeamMembership() {
        return this.teamMembership;
    }

    /**
     * @return the avatar
     */
    public DaoFileMetadata getAvatar() {
        return this.avatar;
    }

    /**
     * @param avatar
     */
    public void setAvatar(final DaoFileMetadata avatar) {
        this.avatar = avatar;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoMember() {
        super();
    }
}
