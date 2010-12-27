package com.bloatit.model.data;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

/**
 * A translation store the data of a description in a for a locale.
 */
@Entity
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
    private DaoDescription description;

    /**
     * Create a new translation.
     * 
     * @param member
     * @param description
     * @param locale
     * @param title
     * @param text
     * @throws NullPointerException if any of the field is null
     */
    public DaoTranslation(final DaoMember member, final DaoDescription description, final Locale locale, final String title, final String text) {
        super(member);
        if (description == null || locale == null || title == null || text == null) {
            throw new NullPointerException();
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

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoTranslation() {
        super();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setTitle(final String title) {
        this.title = title;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setText(final String text) {
        this.text = text;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoDescription getDescription() {
        return description;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setDescription(final DaoDescription description) {
        this.description = description;
    }
}
