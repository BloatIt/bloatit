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
package com.bloatit.model.managers;

import com.bloatit.data.DaoJoinTeamInvitation;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.lists.TeamList;

/**
 * The Class TeamManager is an utility class containing static methods for
 * {@link Team} loading etc.
 */
public final class TeamManager {

    /**
     * Desactivated constructor on utility class.
     */
    private TeamManager() {
        // Desactivate default ctor
    }

    /**
     * Gets a team using its (unique) name.
     * 
     * @param name the name of the team we are looking for.
     * @return the team found or null if not found.
     */
    public static Team getByName(final String name) {
        return Team.create(DaoTeam.getByName(name));
    }

    /**
     * Tells if a team exist using its name.
     * 
     * @param name the name of the team we are trying to know if it exists.
     * @return true, if it exist, false otherwise.
     */
    public static boolean exist(final String name) {
        return DaoTeam.getByName(name) != null;
    }

    /**
     * Gets the invitation to join a team.
     * 
     * @param team the team this invitation is on.
     * @param member the member this invitation was sent to.
     * @return the invitation, are null if there is no invitation on that
     *         <code>team</code> sent to this <code>member</code>.
     */
    public static JoinTeamInvitation getInvitation(final Team team, final Member member) {
        return JoinTeamInvitation.create(DaoJoinTeamInvitation.getRecievedInvitation(team.getDao(), member.getDao()));
    }

    /**
     * Gets the invitation by id.
     * 
     * @param id the id
     * @return the invitation or null if not found.
     */
    public static JoinTeamInvitation getInvitationById(final int id) {
        return JoinTeamInvitation.create(DBRequests.getById(DaoJoinTeamInvitation.class, id));
    }

    /**
     * Gets the team by id.
     * 
     * @param id the id
     * @return the team or null if not found
     */
    public static Team getById(final int id) {
        return Team.create(DBRequests.getById(DaoTeam.class, id));
    }

    /**
     * Gets the all the teams.
     * 
     * @return the teams.
     */
    public static PageIterable<Team> getAll() {
        return new TeamList(DBRequests.getAll(DaoTeam.class));
    }
}
