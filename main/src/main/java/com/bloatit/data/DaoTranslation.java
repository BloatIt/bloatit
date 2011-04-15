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

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * A translation store the data of a description in a for a locale.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "locale", "description_id" }) })
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoTranslation extends DaoKudosable {

    @Basic(optional = false)
    private Locale locale;

    @Basic(optional = false)
    @Column(columnDefinition = "TEXT")
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String title;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String text;

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoDescription description;

    /**
     * Create a new translation.
     * 
     * @param member
     * @param description
     * @param locale
     * @param title
     * @param text
     * @throws NonOptionalParameterException if any of the field is null
     */
    public DaoTranslation(final DaoMember member, final DaoTeam team, final DaoDescription description, final Locale locale, final String title, final String text) {
        super(member, team);
        if (description == null || locale == null || title == null || text == null) {
            throw new NonOptionalParameterException();
        }
        if (title.isEmpty() || text.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.locale = locale;
        this.title = title;
        this.text = text;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getText() {
        return this.text;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoTranslation() {
        super();
    }

    protected DaoDescription getDescription() {
        return this.description;
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
        int result = super.hashCode();
        result = prime * result + ((this.locale == null) ? 0 : this.locale.hashCode());
        result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
        result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
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
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof DaoTranslation)) {
            return false;
        }
        final DaoTranslation other = (DaoTranslation) obj;
        if (this.locale == null) {
            if (other.locale != null) {
                return false;
            }
        } else if (!this.locale.equals(other.locale)) {
            return false;
        }
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!this.text.equals(other.text)) {
            return false;
        }
        if (this.title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!this.title.equals(other.title)) {
            return false;
        }
        return true;
    }

}
