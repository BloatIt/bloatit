package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.TranslationList;
import com.bloatit.model.data.DaoDescription;

/**
 * A description must be created through the Demand class. (For example, you create a
 * description each time you create a demand.) There is no right management for this
 * class. I assume that if you can get a <code>Description</code> then you can access
 * every property in it.
 *
 * @see DaoDescription
 */
public final class Description extends Identifiable {

    private final DaoDescription dao;

    static Description create(DaoDescription dao) {
        if (dao != null){
            return new Description(dao);
        }
        return null;
    }

    /**
     * Create a Description. If you are looking for a way to create a new description see
     * {@link Demand#addOffer(java.math.BigDecimal, Locale, String, String, java.util.Date)}
     *
     * @param member is the author of this description
     * @param locale is the locale in which the description is written.
     * @param title is the title of the description
     * @param description is the main text of the description (the actual description)
     */
    Description(final Member member, final Locale locale, final String title, final String description) {
        super();
        this.dao = DaoDescription.createAndPersist(member.getDao(), locale, title, description);
    }

    /**
     * Create a description using its dao representation.
     */
    private Description(final DaoDescription dao) {
        super();
        this.dao = dao;
    }

    /**
     * @return the dao representation of this description.
     */
    DaoDescription getDao() {
        return dao;
    }

    /**
     * @return all the translations for a description and <code>this</code> also.
     * @see DaoDescription#getTranslations()
     */
    public PageIterable<Translation> getTranslations() {
        return new TranslationList(dao.getTranslations());
    }

    public void addTranslation(final Translation translation) {
        dao.addTranslation(translation.getDao());
    }

    public Translation getTranslation(final Locale locale) {
        return Translation.create(dao.getTranslation(locale));
    }

    public Translation getTranslationOrDefault(final Locale locale) {
        final Translation tr = getTranslation(locale);
        if (tr == null) {
            return getTranslation(getDefaultLocale());
        }
        return tr;
    }

    public Translation getDefaultTranslation() {
        return Translation.create(dao.getDefaultTranslation());
    }

    public void setDefaultLocale(final Locale defaultLocale) {
        dao.setDefaultLocale(defaultLocale);
    }

    public Locale getDefaultLocale() {
        return dao.getDefaultLocale();
    }

    @Override
    public int getId() {
        return dao.getId();
    }
}
