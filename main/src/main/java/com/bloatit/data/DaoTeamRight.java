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
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * This class is for Hibernate only
 * <p>
 * Describes what a member can do in a given team
 * </p>
 */
@Entity
public class DaoTeamRight extends DaoIdentifiable {

    /**
     * Enumerate what kind of right a user has in a specific group.
     */
    public enum UserTeamRight {
        /**
         * Every member of the team must have this right. It is the basic right
         * to consult the group content.
         */
        CONSULT,
        /**
         * Tells if you can create content in the name of a team.
         */
        TALK,
        /**
         * tells if you can invite a user in the current team
         */
        INVITE,
        /**
         * tells if you can modify content created by a team
         */
        MODIFY,
        /**
         * Tells that you have access to the account of the team.
         */
        BANK,
        /**
         * You are the super user of this team. You can change the rights of
         * other users (but not yours).
         */
        PROMOTE
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DaoTeamMembership membership;

    @Basic(optional = false)
    @Enumerated
    private UserTeamRight userStatus;

    protected DaoTeamRight(final DaoTeamMembership membership, final UserTeamRight userStatus) {
        super();
        this.membership = membership;
        this.userStatus = userStatus;
    }

    protected DaoTeamMembership getMembership() {
        return this.membership;
    }

    protected UserTeamRight getUserStatus() {
        return this.userStatus;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return null;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoTeamRight() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.membership == null) ? 0 : this.membership.hashCode());
        result = prime * result + ((this.userStatus == null) ? 0 : this.userStatus.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoTeamRight other = (DaoTeamRight) obj;
        if (this.membership == null) {
            if (other.membership != null) {
                return false;
            }
        } else if (!this.membership.equals(other.membership)) {
            return false;
        }
        if (this.userStatus != other.userStatus) {
            return false;
        }
        return true;
    }
}
