package com.bloatit.model.data;

import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.bloatit.model.data.util.NonOptionalParameterException;

/**
 * A translation store the data of a description in a for a locale.
 */
@Entity
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
        setLocale(locale);
        setTitle(title);
        setText(text);
        setDescription(description);
    }

    public final String getTitle() {
        return title;
    }

    public final Locale getLocale() {
        return locale;
    }

    public final String getText() {
        return text;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    private void setTitle(final String title) {
        this.title = title;
    }

    private void setLocale(final Locale locale) {
        this.locale = locale;
    }

    private void setText(final String text) {
        this.text = text;
    }

    private void setDescription(final DaoDescription description) {
        this.description = description;
    }

    protected DaoTranslation() {
        super();
    }

    @SuppressWarnings("unused")
    private DaoDescription getDescription() {
        return description;
    }
}
