package com.bloatit.data;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.bloatit.framework.exceptions.NonOptionalParameterException;

/**
 * A translation store the data of a description in a for a locale.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "locale", "description_id" }) })
public final class DaoTranslation extends DaoKudosable {

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
    public DaoTranslation(final DaoMember member, final DaoDescription description, final Locale locale, final String title, final String text) {
        super(member);
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
        return title;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getText() {
        return text;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoTranslation() {
        super();
    }

    protected DaoDescription getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        if (locale == null) {
            if (other.locale != null) {
                return false;
            }
        } else if (!locale.equals(other.locale)) {
            return false;
        }
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

}
