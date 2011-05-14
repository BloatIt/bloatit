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

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.datetime.DateUtils;

/**
 * A team is an entity where people can be team...
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                            name = "team.byName",
                            query = "FROM DaoTeam WHERE login = :login"),
                        @NamedQuery(
                            name = "team.getContributions",
                            query = "FROM DaoContribution WHERE asTeam = :this "),
                        @NamedQuery(
                            name = "team.getContributions.size",
                            query = "SELECT count(*) FROM DaoContribution WHERE asTeam = :this "),
                        @NamedQuery(
                            name = "team.getActivity",
                            query = "FROM DaoUserContent as u " +
                                    "WHERE u.asTeam = :team " +
                                    "AND id not in (from DaoKudos) " +
                                    "AND id not in (from DaoTranslation)"  +
                                    "ORDER BY creationDate DESC"),
                        @NamedQuery(
                            name = "team.getActivity.size",
                            query = "SELECT COUNT(*)" +
                            		"FROM DaoUserContent as u " +
                                    "WHERE u.asTeam = :team " +
                                    "AND id not in (from DaoKudos) " +
                                    "AND id not in (from DaoTranslation)"),
                        @NamedQuery(
                            name = "team.getRecentActivity.size",
                            query = "SELECT COUNT(*)" +
                                    "FROM DaoUserContent as u " +
                                    "WHERE u.asTeam = :team " +
                                    "AND id not in (from DaoKudos) " +
                                    "AND id not in (from DaoTranslation)" +
                                    "AND creationDate > :date"),
                        @NamedQuery(
                                name = "team.getUserTeamRights",
                                query = "SELECT gm " +
                                		"FROM com.bloatit.data.DaoTeam g " +
                                		"JOIN g.teamMembership as gm " +
                                		"JOIN gm.member as m " +
                                		"WHERE g = :team " +
                                		"AND m = :member"),
                		@NamedQuery(
                		        name = "team.getUserTeamRights.size",
                		        query = "SELECT count(gm) " +
                    		            "FROM com.bloatit.data.DaoTeam g " +
                    		            "JOIN g.teamMembership as gm " +
                    		            "JOIN gm.member as m " +
                    		            "WHERE g = :team " +
                    		            "AND m = :member"),
    		            @NamedQuery(
	                        name = "team.getMoneyWithdrawal",
	                        query = "from DaoMoneyWithdrawal as x where x.actor = :actor"),
    		            @NamedQuery(
	                        name = "team.getMoneyWithdrawal.size",
	                        query = "select count(*) from DaoMoneyWithdrawal as x where x.actor = :actor"),
                       }
             )
// @formatter:on
public class DaoTeam extends DaoActor {

    /**
     * There is 2 kinds of teams : The PUBLIC that everybody can see and and go
     * in. The PROTECTED that everybody can see, but require an invitation to go
     * in.
     */
    //TODO: rename to visibility or somethings else
    public enum Right {
        /**
         * Everybody can see this team and can go into it.
         */
        PUBLIC,
        /**
         * Everybody can see this team but you need an invitation to go into it.
         */
        PROTECTED;
    }

    /**
     * WARNING right is a SQL keyword. This is mapped as "team_right".
     */
    @Basic(optional = false)
    @Column(name = "team_right")
    private Right right;

    @Basic(optional = false)
    @Column(unique = false)
    private String contact;

    @Basic(optional = false)
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = true, cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private DaoFileMetadata avatar;

    @OneToMany(mappedBy = "bloatitTeam")
    @Cascade(value = { CascadeType.ALL })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private final List<DaoTeamMembership> teamMembership = new ArrayList<DaoTeamMembership>(0);

    // ======================================================================
    // Static HQL Requests
    // ======================================================================

    /**
     * Get a team using its name.
     *
     * @param name the name of the team we are lookong for.
     * @return the team named <code>name<code> or null if not found.
     */
    public static DaoTeam getByName(final String name) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.getNamedQuery("team.byName");
        q.setString("login", name);
        return (DaoTeam) q.uniqueResult();
    }

    // ======================================================================
    // Construction
    // ======================================================================

    /**
     * Create a team and add it into the db.
     *
     * @param login it the unique and non updatable name of the team.
     * @param contact some contact information for that team.
     * @param description the desctiption of the team.
     * @param right is the type of team we are creating (Public or Private).
     * @return the newly created team.
     * @throws HibernateException
     */
    public static DaoTeam createAndPersiste(final String login, final String contact, final String description, final Right right) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoTeam team = new DaoTeam(login, contact, description, right);
        try {
            session.save(team);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return team;
    }

    /**
     * Create a DaoTeam
     *
     * @param login is the name of the team. It must be unique.
     * @param contact ...
     * @param right is the default right value for this team.
     */
    private DaoTeam(final String login, final String contact, final String description, final Right right) {
        super(login);
        if (right == null || contact == null || contact.isEmpty() || description == null) {
            throw new NonOptionalParameterException();
        }
        this.right = right;
        this.contact = contact;
        this.description = description;
    }

    /**
     * Change the type of team.
     *
     * @param right the new right of this team.
     */
    public void setRight(final Right right) {
        this.right = right;
    }

    /**
     * Change the contact information on this team.
     *
     * @param contact the new contact information.
     */
    public void setContact(final String contact) {
        this.contact = contact;
    }

    /**
     * Add a member in this team.
     *
     * @param member The member to add
     * @param isAdmin true if the member need to have the right to administer
     *            this team. (This may change if the number of role change !)
     */
    public void addMember(final DaoMember member, final boolean isAdmin) {
        this.teamMembership.add(new DaoTeamMembership(member, this));
    }

    /**
     * Remove a member from the team
     *
     * @param member the member to remove
     */
    protected void removeMember(final DaoMember member) {
        final DaoTeamMembership link = DaoTeamMembership.get(this, member);
        this.teamMembership.remove(link);
        member.getTeamMembership().remove(link);
        SessionManager.getSessionFactory().getCurrentSession().delete(link);
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

    /**
     * @param description the new description of the team.
     */
    public void setDescription(final String description) {
        this.description = description;
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

    /**
     * @return the contribution done by this team.
     */
    public PageIterable<DaoContribution> getContributions() {
        return new QueryCollection<DaoContribution>("team.getContributions").setEntity("this", this);
    }

    /**
     * Gets the money withdrawals.
     *
     * @return all the money withdrawals from this team.
     */
    public PageIterable<DaoMoneyWithdrawal> getMoneyWithdrawals() {
        return new QueryCollection<DaoMoneyWithdrawal>("team.getMoneyWithdrawal").setEntity("actor", this);
    }

    /**
     * @return all the member in this team. (Use a HQL query).
     */
    public PageIterable<DaoMember> getMembers() {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query filter = session.createFilter(getTeamMembership(), "select this.member order by login");
        final Query count = session.createFilter(getTeamMembership(), "select count(*)");
        return new QueryCollection<DaoMember>(filter, count);
    }

    /**
     * @return the type of team (PUBLIC or PRIVATE)
     */
    public Right getRight() {
        return this.right;
    }

    /**
     * Finds if a member is in this team, and which is its status.
     *
     * @param member The member we want to know its status.
     * @return {@code null} if the member is not in this team, or a set
     *         otherwise. <br />
     *         Note, the returned set can be empty if the user is only a Member
     */
    public EnumSet<UserTeamRight> getUserTeamRight(final DaoMember member) {
        final Query q = SessionManager.getNamedQuery("team.getUserTeamRights");
        q.setEntity("member", member);
        q.setEntity("team", this);
        final DaoTeamMembership gm = (DaoTeamMembership) q.uniqueResult();
        final EnumSet<UserTeamRight> rights = EnumSet.noneOf(UserTeamRight.class);
        if (gm == null || gm.getRights() == null) {
            return rights;
        }
        for (final DaoTeamRight teamRight : gm.getRights()) {
            rights.add(teamRight.getUserStatus());
        }
        return rights;
    }

    /**
     * @return the description of this team.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the contact informations of this team
     */
    public String getContact() {
        return this.contact;
    }

    /**
     * Used in DaoMember.
     */
    protected List<DaoTeamMembership> getTeamMembership() {
        return this.teamMembership;
    }

    public PageIterable<DaoUserContent> getActivity() {
        final Query query = SessionManager.getNamedQuery("team.getActivity");
        final Query size = SessionManager.getNamedQuery("team.getActivity.size");

        final QueryCollection<DaoUserContent> q = new QueryCollection<DaoUserContent>(query, size);
        q.setEntity("team", this);
        return q;
    }

    public long getRecentActivityCount(final int numberOfDays) {
        final Query size = SessionManager.getNamedQuery("team.getRecentActivity.size");
        size.setEntity("team", this);
        size.setDate("date", DateUtils.nowMinusSomeDays(numberOfDays));
        return (Long) size.uniqueResult();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoTeam() {
        super();
    }
}
