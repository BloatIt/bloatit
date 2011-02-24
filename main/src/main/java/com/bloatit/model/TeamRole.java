package com.bloatit.model;

import java.util.EnumSet;
import java.util.Set;

import com.bloatit.data.DaoGroupRight.UserGroupRight;

/**
 * A class used to describe various actions possible by group members
 */
public class TeamRole {
    private final Set<UserGroupRight> rights;

    public final static TeamRole MEMBER = new TeamRole(EnumSet.of(UserGroupRight.CONSULT));
    public final static TeamRole DEVELOPER = new TeamRole(EnumSet.range(UserGroupRight.CONSULT, UserGroupRight.TALK));
    public final static TeamRole ADMIN = new TeamRole(EnumSet.allOf(UserGroupRight.class));

    public TeamRole(final Set<UserGroupRight> rights) {
        this.rights = rights;
    }

    /**
     * Indicates whether the user with the role can consult group information
     *
     * @return <code>true</code> if the user can consult group information,
     *         <code>false</code> otherwise
     */
    public boolean consult() {
        return rights.contains(UserGroupRight.CONSULT);
    }

    /**
     * Indicates whether the user with the role can talk for the group
     *
     * @return <code>true</code> if the user can talk for the group,
     *         <code>false</code> otherwise
     */
    public boolean talk() {
        return rights.contains(UserGroupRight.TALK);
    }

    /**
     * Indicates whether the user with the role can invite people into the group
     *
     * @return <code>true</code> if the user can invite people into the group,
     *         <code>false</code> otherwise
     */
    public boolean invite() {
        return rights.contains(UserGroupRight.INVITE);
    }

    /**
     * Indicates whether the user with the role can modify group information
     *
     * @return <code>true</code> if the user can modify group information,
     *         <code>false</code> otherwise
     */
    public boolean modify() {
        return rights.contains(UserGroupRight.MODIFY);
    }

    /**
     * Indicates whether the user with the role can change the role of people in
     * the group
     * <p>
     * Note : giving promoting rights means the user can also demote
     * </p>
     *
     * @return <code>true</code> if the user can change the role of people,
     *         <code>false</code> otherwise
     */
    public boolean promote() {
        return rights.contains(UserGroupRight.PROMOTE);
    }

    /**
     * Indicates whether the user with the role have access to bank information
     * (withdraw money ...)
     *
     * @return <code>true</code> if the user can access bank information,
     *         <code>false</code> otherwise
     */
    public boolean bank() {
        return rights.contains(UserGroupRight.BANK);
    }

    /**
     * Find all the rights for this role
     *
     * @return a set containing every right for this role
     */
    public Set<UserGroupRight> getRights() {
        return rights;
    }
}
