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
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.UserContent;

/**
 * A user content is a content created by a user. A user content as an Author,
 * and can be posted in the name of a team. It also has a creation date.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@FilterDef(name="usercontent.nonDeleted" )
@Filters( {
    @Filter(name="usercontent.nonDeleted", condition="isDeleted = 'false'"),
} )
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class DaoUserContent extends DaoIdentifiable {

    /**
     * This is the author of the user content.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoMember member;

    /**
     * Most of the time this is null. But when a user create a content in the
     * name of a team, asTeam point on it.
     */
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private DaoTeam asTeam;

    @Basic(optional = false)
    @Field(store = Store.NO)
    private Date creationDate;

    /**
     * Instead of actually deleting the content in the db, we can set the
     * isDeleted boolean to true.
     */
    @Basic(optional = false)
    @Field(store = Store.YES)
    private boolean isDeleted;

    /**
     * A user can attach on any userContent a file.
     */
    @OneToMany(mappedBy = "relatedContent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoFileMetadata> files = new ArrayList<DaoFileMetadata>();

    /**
     * Initialize the creation date to now.
     * 
     * @param member is the author of this UserContent.
     * @param team can be null. If the content is created in the name of a team,
     *            then you have to specify the team here.
     * @throws NonOptionalParameterException if the member == null.
     */
    protected DaoUserContent(final DaoMember member, final DaoTeam team) {
        super();
        if (member == null) {
            Log.data().fatal("Cannot create a DaoUserContent with a null member.");
            throw new NonOptionalParameterException();
        }
        this.member = member;
        this.asTeam = team;
        this.creationDate = new Date();
        setIsDeleted(false);
    }

    /**
     * Gets the member that created this {@link UserContent}.
     * 
     * @return the member that created this {@link UserContent}.
     */
    public DaoMember getMember() {
        return this.member;
    }

    /**
     * Get the author. It can be the member or the team if this
     * {@link UserContent} has been created as a team.
     * 
     * @return the author (member or team).
     */
    public final DaoActor getAuthor() {
        return asTeam == null ? member : asTeam;
    }

    /**
     * @return The creation date of this content.
     */
    public final Date getCreationDate() {
        return (Date) this.creationDate.clone();
    }

    /**
     * @return the team in which name this content has been created or null.
     */
    public final DaoTeam getAsTeam() {
        return this.asTeam;
    }

    /**
     * @return all the files associated with this DaoUserContent.
     */
    public PageIterable<DaoFileMetadata> getFiles() {
        return new MappedList<DaoFileMetadata>(this.files);
    }

    /**
     * @return true if the content should be considered as delete.
     */
    public boolean isDeleted() {
        return this.isDeleted;
    }

    /**
     * @param isDeleted the new isDeleted value.
     * @see #isDeleted
     */
    public void setIsDeleted(final Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Attach a file on this user content.
     * 
     * @param daoFileMetadata the file to attach on this content
     */
    public void addFile(final DaoFileMetadata daoFileMetadata) {
        this.files.add(daoFileMetadata);
        daoFileMetadata.setRelatedContent(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoUserContent() {
        super();
    }

    // ======================================================================
    // hashcode and equals
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.asTeam == null) ? 0 : this.asTeam.hashCode());
        result = prime * result + ((this.creationDate == null) ? 0 : this.creationDate.hashCode());
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
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DaoUserContent)) {
            return false;
        }
        final DaoUserContent other = (DaoUserContent) obj;
        if (this.asTeam == null) {
            if (other.asTeam != null) {
                return false;
            }
        } else if (!this.asTeam.equals(other.asTeam)) {
            return false;
        }
        if (this.creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!this.creationDate.equals(other.creationDate)) {
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
