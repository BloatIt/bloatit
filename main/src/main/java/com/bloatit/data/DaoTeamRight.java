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

    public enum UserTeamRight {
        CONSULT, TALK, INVITE, MODIFY, BANK, PROMOTE
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
