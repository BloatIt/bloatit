package com.bloatit.model;

import java.util.EnumSet;
import java.util.Set;

import com.bloatit.data.DaoTeamRight.UserTeamRight;

/**
 * A class used to describe various actions possible by team members
 */
public class TeamRole {
    private final Set<UserTeamRight> rights;

    public final static TeamRole MEMBER = new TeamRole(EnumSet.of(UserTeamRight.CONSULT));
    public final static TeamRole DEVELOPER = new TeamRole(EnumSet.range(UserTeamRight.CONSULT, UserTeamRight.TALK));
    public final static TeamRole ADMIN = new TeamRole(EnumSet.allOf(UserTeamRight.class));

    public TeamRole(final Set<UserTeamRight> rights) {
        this.rights = rights;
    }

    /**
     * Indicates whether the user with the role can consult team information
     *
     * @return <code>true</code> if the user can consult team information,
     *         <code>false</code> otherwise
     */
    public boolean consult() {
        return rights.contains(UserTeamRight.CONSULT);
    }

    /**
     * Indicates whether the user with the role can talk for the team
     *
     * @return <code>true</code> if the user can talk for the team,
     *         <code>false</code> otherwise
     */
    public boolean talk() {
        return rights.contains(UserTeamRight.TALK);
    }

    /**
     * Indicates whether the user with the role can invite people into the team
     *
     * @return <code>true</code> if the user can invite people into the team,
     *         <code>false</code> otherwise
     */
    public boolean invite() {
        return rights.contains(UserTeamRight.INVITE);
    }

    /**
     * Indicates whether the user with the role can modify team information
     *
     * @return <code>true</code> if the user can modify team information,
     *         <code>false</code> otherwise
     */
    public boolean modify() {
        return rights.contains(UserTeamRight.MODIFY);
    }

    /**
     * Indicates whether the user with the role can change the role of people in
     * the team
     * <p>
     * Note : giving promoting rights means the user can also demote
     * </p>
     *
     * @return <code>true</code> if the user can change the role of people,
     *         <code>false</code> otherwise
     */
    public boolean promote() {
        return rights.contains(UserTeamRight.PROMOTE);
    }

    /**
     * Indicates whether the user with the role have access to bank information
     * (withdraw money ...)
     *
     * @return <code>true</code> if the user can access bank information,
     *         <code>false</code> otherwise
     */
    public boolean bank() {
        return rights.contains(UserTeamRight.BANK);
    }

    /**
     * Find all the rights for this role
     *
     * @return a set containing every right for this role
     */
    public Set<UserTeamRight> getRights() {
        return rights;
    }
}
