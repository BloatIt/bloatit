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
import java.util.EnumSet;
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
import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.data.DaoJoinTeamInvitation.State;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.MalformedArgumentException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.model.ModelConfiguration;

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
                                   name = "member.byEmail.size",
                                   query = "select count(*) from DaoMember where email = :email"),
                       @NamedQuery(
                                   name = "member.byEmailToActivate.size",
                                   query = "select count(*) from DaoMember where emailToActivate = :emailToActivate"),
                       @NamedQuery(
                           name = "member.byLoginPassword",
                           query = "FROM DaoMember WHERE login = :login AND password = :password"),

                       @NamedQuery(
                           name = "member.byEmail",
                           query = "FROM DaoMember WHERE email = :email"),

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

                       @NamedQuery(
                           name = "member.getActivity",
                           query = "FROM DaoUserContent as u " +
                                   "WHERE u.member = :member " +
                                   "AND id not in (from DaoKudos) " +
                                   "AND id not in (from DaoTranslation)"  +
                                   "AND id not in (from DaoExternalService)"  +
                                   "ORDER BY creationDate DESC"),

                       @NamedQuery(
                           name = "member.getActivity.size",
                           query = "SELECT COUNT(*)" +
                           		   "FROM DaoUserContent as u " +
                                   "WHERE u.member = :member " +
                                   "AND id not in (from DaoKudos) " +
                                   "AND id not in (from DaoExternalService)"  +
                                   "AND id not in (from DaoTranslation)"),
                        @NamedQuery(
                            name = "member.invitationCount",
                            query = "SELECT COUNT(*)" +
                                    "FROM DaoJoinTeamInvitation as i " +
                                    "WHERE i.receiver = :member " +
                                    "AND i.state = :state"),
                        @NamedQuery(
                                    name = "member.getMilestoneToInvoice",
                                    query = "SELECT bs " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "WHERE offer_.member = :this " +
                                    "AND offer_.asTeam = null " +
                                    "AND bs.milestoneState = :state " +
                                    "AND bs.invoices IS EMPTY"),
                        @NamedQuery(
                                    name = "member.getMilestoneToInvoice.size",
                                    query = "SELECT count(*) " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "WHERE offer_.member = :this " +
                                    "AND offer_.asTeam = null " +
                                    "AND bs.milestoneState = :state " +
                                    "AND bs.invoices IS EMPTY"),
                        @NamedQuery(
                                    name = "member.getMilestones",
                                    query = "SELECT bs " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "WHERE offer_.member = :this " +
                                    "AND offer_.asTeam = null "),
                        @NamedQuery(
                                    name = "member.getMilestones.size",
                                    query = "SELECT count(*) " +
                                    "FROM com.bloatit.data.DaoOffer offer_ " +
                                    "JOIN offer_.milestones as bs " +
                                    "WHERE offer_.member = :this " +
                                    "AND offer_.asTeam = null "),
                        @NamedQuery(
                                    name = "members.exceptRole",
                                    query = "FROM com.bloatit.data.DaoMember " +
                                            "WHERE role != :role " +
                                            "AND karma > :threshold " +
                                            "ORDER BY CONCAT(coalesce(fullname, ''), login) ASC"),
                        @NamedQuery(
                                    name = "members.exceptRole.size",
                                    query = "SELECT count(*) " +
                                            "FROM com.bloatit.data.DaoMember " +
                                            "WHERE role != :role " +
                                            "AND karma > :threshold "),
                        @NamedQuery(
                                    name =  "member.getFollowedActor.byActor",
                                    query = "FROM com.bloatit.data.DaoFollowActor " +
                                            "WHERE follower = :member " +
                                            "AND followed = :actor "),
                        @NamedQuery(
                                    name =  "member.getFollowedActor.bySoftware",
                                    query = "FROM com.bloatit.data.DaoFollowSoftware " +
                                            "WHERE follower = :member " +
                                            "AND followed = :software "),
                        @NamedQuery(
                                    name =  "member.getFollowedActor.byFeature",
                                    query = "FROM com.bloatit.data.DaoFollowFeature " +
                                            "WHERE follower = :member " +
                                            "AND followed = :feature"),
                   }

             )
// @formatter:on
public class DaoMember extends DaoActor {

    /**
     * The Enum Role is the level of power a member has.
     */
    public enum Role {

        /** The NORMAL. */
        NORMAL,
        /** The PRIVILEGED. */
        PRIVILEGED,
        /** The REVIEWER. */
        REVIEWER,
        /** The MODERATOR. */
        MODERATOR,
        /** The ADMIN user can do everything. */
        ADMIN
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

    @Basic(optional = true)
    private String emailToActivate;

    @Basic(optional = false)
    private Locale locale;

    @Basic(optional = false)
    private boolean newsletter;

    @Column(length = 1024)
    private String description;

    @ManyToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private DaoFileMetadata avatar;

    // this property is for hibernate mapping.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoTeamMembership> teamMembership = new ArrayList<DaoTeamMembership>(0);

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private final List<DaoExternalServiceMembership> authorizedExternalServices = new ArrayList<DaoExternalServiceMembership>();

    // Follow System

    @OneToMany(mappedBy = "follower")
    private final List<DaoFollowSoftware> followedSoftware = new ArrayList<DaoFollowSoftware>();

    @OneToMany(mappedBy = "follower")
    private final List<DaoFollowActor> followedActors = new ArrayList<DaoFollowActor>();

    @OneToMany(mappedBy = "follower")
    private final List<DaoFollowFeature> followedFeatures = new ArrayList<DaoFollowFeature>();

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
        if (login == null) {
            return null;
        }
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Criteria q = session.createCriteria(DaoMember.class).add(Restrictions.like("login", login).ignoreCase());
        return (DaoMember) q.uniqueResult();
    }

    /**
     * This method use a HQL request.
     * 
     * @param email the email we are looking for.
     * @return true if found
     */
    public static boolean emailExists(final String email) {
        final Query q1 = SessionManager.getNamedQuery("member.byEmail.size").setString("email", email);
        if (((Long) q1.uniqueResult()) > 0) {
            return true;
        }
        final Query q2 = SessionManager.getNamedQuery("member.byEmailToActivate.size").setString("emailToActivate", email);
        return ((Long) q2.uniqueResult()) > 0;
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

    /**
     * Finds a DaoMember using its email.
     * 
     * @param email the email of the member
     * @return the member matching <code>email</code> or <i>null</i> if not
     *         found
     */
    public static DaoMember getByEmail(final String email) {
        if (email == null) {
            return null;
        }

        final Query q = SessionManager.getNamedQuery("member.byEmail");
        q.setString("email", email);
        return (DaoMember) q.uniqueResult();

    }

    public static PageIterable<DaoMember> getAllMembersButAdmins() {
        return new QueryCollection<DaoMember>("members.exceptRole").setParameter("role", Role.ADMIN)
                                                                   .setInteger("threshold", ModelConfiguration.getKarmaHideThreshold());
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
     * @param salt the salt
     * @param email the email
     * @param locale the locale of the user.
     * @return The newly created DaoMember
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
        if (!login.matches("[^\\p{Space}]+")) {
            throw new MalformedArgumentException("The login cannot contain space characters.");
        }
        setLocale(locale);
        this.email = email;
        this.role = Role.NORMAL;
        this.state = ActivationState.VALIDATING;
        this.password = password;
        this.salt = salt;
        this.karma = ModelConfiguration.getKarmaInitialInitial();
        this.fullname = "";
        this.description = "";
        this.newsletter = false;
    }

    /**
     * Adds this member to a team.
     * 
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
     * Removes this member from team.
     * 
     * @param aTeam the team from which this member is removed.
     */
    public void removeFromTeam(final DaoTeam aTeam) {
        final DaoTeamMembership link = DaoTeamMembership.get(aTeam, this);
        if (link != null) {
            // Remove all right before deletion
            final Set<UserTeamRight> teamRights = getTeamRights(aTeam);
            for (final UserTeamRight right : teamRights) {
                removeTeamRight(aTeam, right);
            }

            this.teamMembership.remove(link);
            aTeam.getTeamMembership().remove(link);
            SessionManager.getSessionFactory().getCurrentSession().delete(link);
        } else {
            Log.data().error("Try to remove a non existing DaoTeamMembership: team = " + aTeam.getId() + " member = " + getId());
        }
    }

    /**
     * Adds team rights on this member.
     * 
     * @param aTeam the team on which we want this user to have this new rights.
     * @param newRight the new right
     */
    public void addTeamRight(final DaoTeam aTeam, final UserTeamRight newRight) {
        final DaoTeamMembership link = DaoTeamMembership.get(aTeam, this);
        if (link != null) {
            link.addUserRight(newRight);
        } else {
            throw new BadProgrammerException("Trying to give user some rights in a team he doesn't belong");
        }
    }

    /**
     * Gets the team rights.
     * 
     * @param aTeam a team on which this member is.
     * @return the team rights of this member into the team <code>team</code>
     */
    public Set<UserTeamRight> getTeamRights(final DaoTeam aTeam) {
        return aTeam.getUserTeamRight(this);
    }

    /**
     * Removes the team right.
     * 
     * @param aTeam a team on which this member is.
     * @param removeRight the right to remve
     */
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

    /**
     * Sets the role.
     * 
     * @param role the new role
     */
    public void setRole(final Role role) {
        this.role = role;
    }

    /**
     * Sets the activation state.
     * 
     * @param state the new activation state
     */
    public void setActivationState(final ActivationState state) {
        this.state = state;
    }

    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the fullname.
     * 
     * @param firstname the new fullname
     */
    public void setFullname(final String firstname) {
        this.fullname = firstname;
    }

    /**
     * Sets the email.
     * 
     * @param email the new email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Adds to the karma.
     * 
     * @param value the value
     */
    public void addToKarma(final int value) {
        this.karma += value;
    }

    /**
     * Sets the locale.
     * 
     * @param locale the new locale
     */
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

    public void setDescription(String userDescription) {
        this.description = userDescription;
    }

    public void setEmailToActivate(final String emailToActivate) {
        this.emailToActivate = emailToActivate;
    }

    public void addAuthorizedExternalService(final String serviceToken, final String accessToken, final EnumSet<RightLevel> level) {
        DaoExternalServiceMembership existingService = DaoExternalServiceMembership.getByServicetokenMember(serviceToken, this);
        if (existingService == null) {
            DaoExternalService service = DaoExternalService.getByToken(serviceToken);
            if (service != null) {
                this.authorizedExternalServices.add(DaoExternalServiceMembership.createAndPersist(this, service, accessToken, level));
            }
        } else {
            existingService.reset(accessToken, level);
        }
    }

    public void acceptNewsLetter(boolean newsletter) {
        this.newsletter = newsletter;
    }

    public DaoFollowActor getOrCreateFollowedActor(DaoActor actor) {
        final Object followed = SessionManager.getNamedQuery("member.getFollowedActor.byActor")
                                              .setEntity("member", this)
                                              .setEntity("actor", actor)
                                              .uniqueResult();
        if (followed == null) {
            return follow(actor);
        }
        return (DaoFollowActor) followed;
    }

    public DaoFollowFeature getOrCreateFollowedFeature(DaoFeature feature) {
        final DaoFollowFeature followed = (DaoFollowFeature) SessionManager.getNamedQuery("member.getFollowedFeature.byFeature")
                                                                           .setEntity("member", this)
                                                                           .setEntity("feature", feature)
                                                                           .uniqueResult();
        if (followed == null) {
            return follow(feature);
        }
        return followed;
    }

    public DaoFollowSoftware getOrCreateFollowedSoftware(DaoSoftware software) {
        final DaoFollowSoftware followed = (DaoFollowSoftware) SessionManager.getNamedQuery("member.getFollowedSoftware.bySoftware")
                                                                             .setEntity("member", this)
                                                                             .setEntity("software", software)
                                                                             .uniqueResult();
        if (followed == null) {
            return follow(software);
        }
        return followed;
    }

    private DaoFollowFeature follow(DaoFeature feature) {
        final DaoFollowFeature followed = DaoFollowFeature.createAndPersist(this, feature, false, false, false, false);
        followedFeatures.add(followed);
        return followed;
    }

    private DaoFollowSoftware follow(DaoSoftware soft) {
        final DaoFollowSoftware followed = DaoFollowSoftware.createAndPersist(this, soft, false);
        followedSoftware.add(followed);
        return followed;
    }

    private DaoFollowActor follow(DaoActor soft) {
        final DaoFollowActor followed = DaoFollowActor.createAndPersist(this, soft, false);
        followedActors.add(followed);
        return followed;
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

    /**
     * Gets the role.
     * 
     * @return the role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Gets the activation state.
     * 
     * @return the activation state
     */
    public ActivationState getActivationState() {
        return this.state;
    }

    /**
     * Gets the fullname.
     * 
     * @return the fullname
     */
    public String getFullname() {
        return this.fullname;
    }

    /**
     * Gets the salt.
     * 
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password equals.
     * 
     * @param otherPassword the other password
     * @return true, if the otherPassword equals the current password.
     */
    public boolean passwordEquals(final String otherPassword) {
        return password.equals(otherPassword);
    }

    /**
     * Gets the email.
     * 
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the locale.
     * 
     * @return the locale
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * Gets the features.
     * 
     * @param asMemberOnly the result must contains only result that are not
     *            done as name of a team.
     * @return All the features created by this member.
     */
    public PageIterable<DaoFeature> getFeatures(final boolean asMemberOnly) {
        return getUserContent(DaoFeature.class, asMemberOnly);
    }

    /**
     * Gets the kudos.
     * 
     * @return All the kudos created by this member.
     */
    public PageIterable<DaoKudos> getKudos() {
        return getUserContent(DaoKudos.class, false);
    }

    /**
     * Gets the contributions.
     * 
     * @param asMemberOnly the result must contains only result that are not
     *            done as name of a team.
     * @return All the contributions created by this member.
     */
    public PageIterable<DaoContribution> getContributions(final boolean asMemberOnly) {
        return getUserContent(DaoContribution.class, asMemberOnly);
    }

    /**
     * Gets the moneywithdrawals.
     * 
     * @return All the contributions created by this member.
     */
    public PageIterable<DaoMoneyWithdrawal> getMoneyWithdrawals() {

        final Query query = SessionManager.createQuery("from DaoMoneyWithdrawal as x where x.actor = :actor");
        final Query size = SessionManager.createQuery("SELECT count(*) from DaoMoneyWithdrawal as x where x.actor = :actor");

        final QueryCollection<DaoMoneyWithdrawal> q = new QueryCollection<DaoMoneyWithdrawal>(query, size);
        q.setEntity("actor", this);
        return q;
    }

    /**
     * Gets the comments.
     * 
     * @param asMemberOnly the result must contains only result that are not
     *            done as name of a team.
     * @return All the Comments created by this member.
     */
    public PageIterable<DaoComment> getComments(final boolean asMemberOnly) {
        return getUserContent(DaoComment.class, asMemberOnly);
    }

    /**
     * Gets the offers.
     * 
     * @param asMemberOnly the result must contains only result that are not
     *            done as name of a team.
     * @return All the Offers created by this member.
     */
    public PageIterable<DaoOffer> getOffers(final boolean asMemberOnly) {
        return getUserContent(DaoOffer.class, asMemberOnly);
    }

    /**
     * Gets the translations.
     * 
     * @param asMemberOnly the result must contains only result that are not
     *            done as name of a team.
     * @return All the Translations created by this member.
     */
    public PageIterable<DaoTranslation> getTranslations(final boolean asMemberOnly) {
        return getUserContent(DaoTranslation.class, asMemberOnly);
    }

    /**
     * Gets the received invitation.
     * 
     * @param state the state
     * @return All the received invitation to join a team which are in a
     *         specified state
     */
    public PageIterable<DaoJoinTeamInvitation> getReceivedInvitation(final State state) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getReceivedInvitations.byState").setEntity("receiver", this).setParameter("state",
                                                                                                                                            state);
    }

    /**
     * Gets the received invitation.
     * 
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

    public long getInvitationCount() {
        final Query q = SessionManager.getNamedQuery("member.invitationCount");
        q.setEntity("member", this);
        q.setParameter("state", State.PENDING);
        return (Long) q.uniqueResult();
    }

    public boolean getNewsletterAccept() {
        return newsletter;
    }

    /**
     * Gets the sent invitation.
     * 
     * @param state the state
     * @param team the team
     * @return the sent invitation
     */
    public PageIterable<DaoJoinTeamInvitation> getSentInvitation(final State state, final DaoTeam team) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getSentInvitations.byStateTeam").setEntity("sender", this)
                                                                                                  .setParameter("state", state)
                                                                                                  .setEntity("team", team);
    }

    /**
     * Gets the sent invitation.
     * 
     * @param state the state
     * @return All the sent invitation to join a team which are in a specified
     *         state
     */
    public PageIterable<DaoJoinTeamInvitation> getSentInvitation(final State state) {
        return new QueryCollection<DaoJoinTeamInvitation>("member.getSentInvitations.byState").setEntity("sender", this).setEntity("state", state);
    }

    /**
     * Checks if is in team.
     * 
     * @param team the team
     * @return if the current member is in the "team".
     */
    public boolean isInTeam(final DaoTeam team) {
        final Query q = SessionManager.getNamedQuery("member.isInTeam");
        q.setEntity("member", this);
        q.setEntity("team", team);
        return ((Long) q.uniqueResult()) >= 1;
    }

    /**
     * Gets the karma.
     * 
     * @return the karma
     */
    public Integer getKarma() {
        return this.karma;
    }

    /**
     * Finds the user recent activity.
     * 
     * @return the user recent activity
     */
    public PageIterable<DaoUserContent> getActivity() {
        final Query query = SessionManager.getNamedQuery("member.getActivity");
        final Query size = SessionManager.getNamedQuery("member.getActivity.size");

        final QueryCollection<DaoUserContent> q = new QueryCollection<DaoUserContent>(query, size);
        q.setEntity("member", this);
        return q;
    }

    /**
     * Base method to all the get something created by the user.
     * 
     * @param asMemberOnly the result must contains only result that are not
     *            done as name of a team.
     */
    private <T extends DaoUserContent> PageIterable<T> getUserContent(final Class<T> theClass, final boolean asMemberOnly) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(theClass);

        final Query query = SessionManager.createQuery("from " + meta.getEntityName() + " as x where x.member = :author"
                + (asMemberOnly ? " AND x.asTeam = null" : ""));
        final Query size = SessionManager.createQuery("SELECT count(*) from " + meta.getEntityName() + " as x where x.member = :author"
                + (asMemberOnly ? " AND x.asTeam = null" : ""));

        final QueryCollection<T> q = new QueryCollection<T>(query, size);
        q.setEntity("author", this);
        return q;
    }

    /**
     * used by DaoTeam.
     * 
     * @return the team membership
     */
    protected List<DaoTeamMembership> getTeamMembership() {
        return this.teamMembership;
    }

    public PageIterable<DaoMilestone> getMilestoneToInvoice() {
        return new QueryCollection<DaoMilestone>("member.getMilestoneToInvoice").setEntity("this", this)
                                                                                .setParameter("state", DaoMilestone.MilestoneState.VALIDATED);
    }

    public PageIterable<DaoMilestone> getMilestones() {
        return new QueryCollection<DaoMilestone>("member.getMilestones").setEntity("this", this);
    }

    /**
     * Gets the avatar.
     * 
     * @return the avatar
     */
    public DaoFileMetadata getAvatar() {
        return this.avatar;
    }

    /**
     * Sets the avatar.
     * 
     * @param avatar the new avatar
     */
    public void setAvatar(final DaoFileMetadata avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public String getEmailToActivate() {
        return this.emailToActivate;
    }

    public DaoExternalServiceMembership getAuthorizedExternalServices(String serviceToken) {
        return DaoExternalServiceMembership.getByServicetokenMember(serviceToken, this);
    }

    public PageIterable<DaoExternalServiceMembership> getAuthorizedExternalServices() {
        return new MappedList<DaoExternalServiceMembership>(authorizedExternalServices);
    }

    public EnumSet<RightLevel> getExternalServiceRights(final String token) {
        final DaoExternalServiceMembership externalService = (DaoExternalServiceMembership) SessionManager.getNamedQuery("externalServiceMembership.getByToken")
                                                                                                          .setString("token", token)
                                                                                                          .uniqueResult();
        if (externalService == null) {
            return EnumSet.noneOf(RightLevel.class);
        }
        if (externalService.isValid()) {
            return externalService.getLevels();
        }
        return EnumSet.noneOf(RightLevel.class);
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoIdentifiable#accept(com.bloatit.data.DataClassVisitor
     * )
     */
    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao member.
     */
    protected DaoMember() {
        super();
    }
}
