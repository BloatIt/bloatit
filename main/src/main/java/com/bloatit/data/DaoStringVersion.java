package com.bloatit.data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

@Entity
@Embeddable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoStringVersion extends DaoIdentifiable {

    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private Date creationDate;

    @Column(nullable = false)
    private boolean isCompacted;

    @ManyToOne
    private DaoMember author;

    @ManyToOne(optional = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoVersionedString versionedString;

    public DaoStringVersion(final String content, final DaoVersionedString versionedString, final DaoMember author) {
        super();
        if (content == null || versionedString == null) {
            throw new NonOptionalParameterException();
        }
        this.content = content;
        this.versionedString = versionedString;
        this.creationDate = new Date();
        this.author = author;
        this.isCompacted = false;
    }

    public final String getContent() {
        return content;
    }

    public final Date getCreationDate() {
        return creationDate;
    }

    public final DaoVersionedString getVersionedString() {
        return versionedString;
    }

    public DaoMember getAuthor() {
        return author;
    }

    /**
     * WARNING replace endline by '\n' and add a '\n' at the end of the string
     * if there wasn't alredy one.
     * 
     * @param previousVersion
     */
    final void compact(final String previousVersion) {
        if (isCompacted) {
            return;
        }
        final Patch patch = DiffUtils.diff(stringToLines(previousVersion), stringToLines(content));
        final List<String> list = DiffUtils.generateUnifiedDiff("original", "revised", stringToLines(previousVersion), patch, 0);
        final StringBuilder sb = new StringBuilder();
        for (final String string : list) {
            sb.append(string).append("\n");
        }
        content = sb.toString();
        isCompacted = true;
    }

    public String getContentUncompacted(final String previousVersion) {
        if (!isCompacted) {
            return content;
        }

        final Patch patch = DiffUtils.parseUnifiedDiff(stringToLines(content));
        try {
            @SuppressWarnings("unchecked") final List<String> list = (List<String>) patch.applyTo(stringToLines(previousVersion));
            final StringBuilder sb = new StringBuilder();
            for (final String string : list) {
                sb.append(string).append("\n");
            }
            return sb.toString();
        } catch (final PatchFailedException e) {
            throw new BadProgrammerException(e);
        }
    }

    public final boolean isCompacted() {
        return isCompacted;
    }

    private static List<String> stringToLines(final String str) {
        return Arrays.asList(str.split("\\r?\\n|\\r"));
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

    /**
     * Instantiates a new dao kudos.
     */
    protected DaoStringVersion() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
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
        final DaoStringVersion other = (DaoStringVersion) obj;
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (creationDate == null) {
            if (other.creationDate != null) {
                return false;
            }
        } else if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        return true;
    }
}
