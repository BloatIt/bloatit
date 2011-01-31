package com.bloatit.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.Query;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

@Entity
public class DaoBug extends DaoUserContent {

    public enum Level {
        FATAL, MAJOR, MINOR
    }

    public enum State {
        PENDING, DEVELOPING, RESOLVED
    }

    @Basic(optional = false)
    private final String description;

    @Basic(optional = false)
    private final Locale locale;

    @Basic(optional = false)
    @Enumerated
    private Level errorLevel;

    @OneToMany
    @Cascade(value = { CascadeType.ALL })
    @OrderBy("creationDate ASC")
    private final Set<DaoComment> comments = new HashSet<DaoComment>();

    @ManyToOne(optional = false)
    private final DaoBatch batch;

    @Basic(optional = false)
    @Enumerated
    private State state;

    public DaoBug(final DaoMember member, final DaoBatch batch, final String description, final Locale locale, final Level errorLevel) {
        super(member);
        if (description == null || batch == null || locale == null || errorLevel == null || description.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.batch = batch;
        this.description = description;
        this.locale = locale;
        this.errorLevel = errorLevel;
        this.state = State.PENDING;
    }

    public void addComment(final DaoComment comment) {
        comments.add(comment);
    }

    public void setErrorLevel(final Level level) {
        this.errorLevel = level;
    }

    public DaoMember getAssignedTo() {
        return getBatch().getOffer().getAuthor();
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @return the locale
     */
    public final Locale getLocale() {
        return locale;
    }

    /**
     * @return the errorLevel
     */
    public final Level getErrorLevel() {
        return errorLevel;
    }

    public DaoBatch getBatch() {
        return batch;
    }

    public State getState() {
        return state;
    }

    public void setResolved() {
        state = State.RESOLVED;
    }

    public void setDeveloping() {
        state = State.DEVELOPING;
    }

    /**
     * @return the comments
     */
    public final PageIterable<DaoComment> getComments() {
        final Query allComments = SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, "");
        final Query allCommentsSize = SessionManager.getSessionFactory().getCurrentSession().createFilter(comments, "selecte count(*)");
        return new QueryCollection<DaoComment>(allComments, allCommentsSize);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
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
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (locale == null) {
            if (other.locale != null) {
                return false;
            }
        } else if (!locale.equals(other.locale)) {
            return false;
        }
        return true;
    }
}
