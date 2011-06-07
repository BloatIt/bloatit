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
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * Represent a bug on a Released part of a milestone. It is like a bug in a
 * bugtracker.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoBug extends DaoUserContent implements DaoCommentable {

    /**
     * Criticality of the bug. A {@link Level#FATAL} error is a very important
     * error etc.
     */
    public enum Level {

        /** The FATAL. */
        FATAL,
        /** The MAJOR. */
        MAJOR,
        /** The MINOR. */
        MINOR
    }

    /**
     * It is the state of the bug ... The developer change it during the process
     * of fixing it.
     */
    public enum BugState {

        /** The PENDING. */
        PENDING,
        /** The DEVELOPING. */
        DEVELOPING,
        /** The RESOLVED. */
        RESOLVED
    }

    @Basic(optional = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    @Basic(optional = false)
    private String description;

    /**
     * This is the language in which the description is written.
     */
    @Basic(optional = false)
    private Locale locale;

    @Basic(optional = false)
    @Enumerated
    private Level level;

    @OneToMany(mappedBy = "bug")
    @Cascade(value = { CascadeType.ALL })
    @OrderBy(clause = "id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final List<DaoComment> comments = new ArrayList<DaoComment>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DaoMilestone milestone;

    @Basic(optional = false)
    @Enumerated
    private BugState state;

    private DaoBug(final DaoMember member,
                   final DaoTeam team,
                   final DaoMilestone milestone,
                   final String title,
                   final String description,
                   final Locale locale,
                   final Level level) {
        super(member, team);
        if (title == null || description == null || milestone == null || locale == null || level == null || description.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.milestone = milestone;
        this.title = title;
        this.description = description;
        this.locale = locale;
        this.level = level;
        this.state = BugState.PENDING;
    }

    /**
     * Creates the bug and persist it.
     *
     * @param member the author
     * @param team the as Team property. can be null.
     * @param milestone the milestone on which there is a bug.
     * @param title the title of the bug
     * @param description the description of the bug
     * @param locale the locale in which this bug has been written
     * @param level the level of the bug
     * @return the new dao bug
     */
    public static DaoBug createAndPersist(final DaoMember member,
                                          final DaoTeam team,
                                          final DaoMilestone milestone,
                                          final String title,
                                          final String description,
                                          final Locale locale,
                                          final Level level) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoBug bug = new DaoBug(member, team, milestone, title, description, locale, level);
        try {
            session.save(bug);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return bug;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoCommentable#addComment(com.bloatit.data.DaoComment)
     */
    @Override
    public void addComment(final DaoComment comment) {
        this.comments.add(comment);
    }

    /**
     * Sets the error level.
     *
     * @param level the new error level
     */
    public void setErrorLevel(final Level level) {
        this.level = level;
    }

    /**
     * The person assigned to a bug is the developer (the member that has
     * created the offer). The person assigned to a bug is the developer (the
     * member that has created the offer).
     *
     * @return the member assigned to this bug.
     */
    public DaoMember getAssignedTo() {
        return getMilestone().getOffer().getMember();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the this is the language in which the description is written.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * Gets the error level.
     *
     * @return the errorLevel
     */
    public Level getErrorLevel() {
        return this.level;
    }

    /**
     * Gets the milestone.
     *
     * @return the milestone
     */
    public DaoMilestone getMilestone() {
        return this.milestone;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public BugState getState() {
        return this.state;
    }

    /**
     * Sets the resolved.
     */
    public void setResolved() {
        this.state = BugState.RESOLVED;
    }

    /**
     * Sets the developing.
     */
    public void setDeveloping() {
        this.state = BugState.DEVELOPING;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    @Override
    public PageIterable<DaoComment> getComments() {
        return CommentManager.getComments(this.comments);
    }

    /**
     * Gets the last comment.
     *
     * @return the last comment
     */
    @Override
    public DaoComment getLastComment() {
        return CommentManager.getLastComment(this.comments);
    }
    
    // ======================================================================
    // Visitor.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoIdentifiable#accept(com.bloatit.data.DataClassVisitor
     * )
     */
    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // Hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao bug.
     */
    protected DaoBug() {
        super();
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.locale == null) ? 0 : this.locale.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoBug other = (DaoBug) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.locale == null) {
            if (other.locale != null) {
                return false;
            }
        } else if (!this.locale.equals(other.locale)) {
            return false;
        }
        return true;
    }
}
