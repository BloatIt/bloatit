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

import com.bloatit.data.DaoGroupRight.UserGroupRight;

/**
 * This class is for Hibernate only.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "bloatitGroup_id" }) })
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@formatter:off
@NamedQueries(value = { @NamedQuery(
                           name = "groupMembership.byGroupMember",
                           query = "FROM DaoGroupMembership gm WHERE gm.bloatitGroup = :bloatitGroup AND gm.member = :member")
                      }
             )
// @formatter:on
class DaoGroupMembership extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMember member;

    @ManyToOne(optional = false)
    private DaoGroup bloatitGroup;

    @OneToMany(mappedBy = "membership", orphanRemoval = true, cascade = { CascadeType.ALL })
    @Cascade(value = {})
    private List<DaoGroupRight> memberRight = new ArrayList<DaoGroupRight>(0);

    /**
     * Get a GroupMembership line using its composite key. (HQL request)
     */
    protected static DaoGroupMembership get(final DaoGroup group, final DaoMember member) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final Query q = session.getNamedQuery("groupMembership.byGroupMember");
        q.setEntity("bloatitGroup", group);
        q.setEntity("member", member);
        return (DaoGroupMembership) q.uniqueResult();
    }

    protected DaoGroupMembership(final DaoMember member, final DaoGroup group) {
        this.member = member;
        this.bloatitGroup = group;
    }

    protected DaoMember getMember() {
        return this.member;
    }

    protected DaoGroup getGroup() {
        return this.bloatitGroup;
    }

    protected List<DaoGroupRight> getRights() {
        return this.memberRight;
    }

    protected void addUserRight(final UserGroupRight newRight) {
        this.memberRight.add(new DaoGroupRight(this, newRight));
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

    protected DaoGroupMembership() {
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
        result = prime * result + ((this.bloatitGroup == null) ? 0 : this.bloatitGroup.hashCode());
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
        if (!(obj instanceof DaoGroupMembership)) {
            return false;
        }
        final DaoGroupMembership other = (DaoGroupMembership) obj;
        if (this.bloatitGroup == null) {
            if (other.bloatitGroup != null) {
                return false;
            }
        } else if (!this.bloatitGroup.equals(other.bloatitGroup)) {
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
