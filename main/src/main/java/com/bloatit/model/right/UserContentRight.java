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
package com.bloatit.model.right;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.model.Team;
import com.bloatit.model.UserContent;

/**
 * The Class UserContentRight store the properties accessor for the
 * {@link UserContent} class.
 */
public class UserContentRight extends RightManager {

    /**
     * The Class AsTeam is an accessor for the AsTeam property.
     */
    public static class AsTeam extends Accessor {

        private final Team team;

        // TODO document
        public AsTeam(Team team) {
            this.team = team;
        }

        /*
         * (non-Javadoc)
         * @see com.bloatit.model.right.Accessor#can(com.bloatit.model.right.
         * RestrictedInterface , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || (team.hasTeamPrivilege(UserTeamRight.TALK) && canWrite(action));
        }
    }
}
