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

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.bloatit.data.DaoTeamRight.UserTeamRight;

/**
 * This class is for Hibernate only.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "bloatitTeam_id" }) })
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "teamMembership.byTeamMember",
                           query = "FROM DaoTeamMembership gm WHERE gm.bloatitTeam = :bloatitTeam AND gm.member = :member")
                      }
             )
// @formatter:on
class DaoTeamMembership extends DaoIdentifiable {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DaoMember member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DaoTeam bloatitTeam;

    @OneToMany(mappedBy = "membership", orphanRemoval = true, cascade = { CascadeType.ALL })
    @Cascade(value = {})
    private final List<DaoTeamRight> memberRight = new ArrayList<DaoTeamRight>(0);

    /**
     * Get a TeamMembership line using its composite key. (HQL request)
     */
    protected static DaoTeamMembership get(final DaoTeam team, final DaoMember member) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.getNamedQuery("teamMembership.byTeamMember");
        q.setEntity("bloatitTeam", team);
        q.setEntity("member", member);
        return (DaoTeamMembership) q.uniqueResult();
    }

    protected DaoTeamMembership(final DaoMember member, final DaoTeam team) {
        this.member = member;
        this.bloatitTeam = team;
    }

    protected DaoMember getMember() {
        return this.member;
    }

    protected DaoTeam getTeam() {
        return this.bloatitTeam;
    }

    protected List<DaoTeamRight> getRights() {
        return this.memberRight;
    }

    protected void addUserRight(final UserTeamRight newRight) {
        this.memberRight.add(new DaoTeamRight(this, newRight));
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

    protected DaoTeamMembership() {
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
        result = prime * result + ((this.bloatitTeam == null) ? 0 : this.bloatitTeam.hashCode());
        result = prime * result + ((this.member == null) ? 0 : this.member.hashCode());
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
        if (!(obj instanceof DaoTeamMembership)) {
            return false;
        }
        final DaoTeamMembership other = (DaoTeamMembership) obj;
        if (this.bloatitTeam == null) {
            if (other.bloatitTeam != null) {
                return false;
            }
        } else if (!this.bloatitTeam.equals(other.bloatitTeam)) {
            return false;
        }
        if (this.member == null) {
            if (other.member != null) {
                return false;
            }
        } else if (!this.member.equals(other.member)) {
            return false;
        }
        return true;
    }
}
