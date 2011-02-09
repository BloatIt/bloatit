package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoDescription;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.model.lists.TranslationList;

/**
 * A description must be created through the DemandImplementation class. (For example, you create a
 * description each time you create a demandImplementation.) There is no right management for this
 * class. I assume that if you can get a <code>Description</code> then you can access
 * every property in it.
 *
 * @see DaoDescription
 */
public final class Description extends Identifiable<DaoDescription> {

    public static Description create(final DaoDescription dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoDescription> created = CacheManager.get(dao);
            if (created == null) {
                return new Description(dao);
            }
            return (Description) created;
        }
        return null;
    }

    /**
     * Create a Description. If you are looking for a way to create a new description see
     * {@link DemandImplementation#addOffer(java.math.BigDecimal, Locale, String, String, java.util.Date)}
     *
     * @param member is the author of this description
     * @param locale is the locale in which the description is written.
     * @param title is the title of the description
     * @param description is the main text of the description (the actual description)
     */
    public Description(final Member member, final Locale locale, final String title, final String description) {
        super(DaoDescription.createAndPersist(member.getDao(), locale, title, description));
    }

    /**
     * Create a description using its dao representation.
     */
    private Description(final DaoDescription dao) {
        super(dao);
    }

    /**
     * @return all the translations for a description and <code>this</code> also.
     * @see DaoDescription#getTranslations()
     */
    public PageIterable<Translation> getTranslations() {
        return new TranslationList(getDao().getTranslations());
    }

    public void addTranslation(final Translation translation) {
        getDao().addTranslation(translation.getDao());
    }

    public Translation getTranslation(final Locale locale) {
        return Translation.create(getDao().getTranslation(locale));
    }

    public Translation getTranslationOrDefault(final Locale locale) {
        final Translation tr = getTranslation(locale);
        if (tr == null) {
            return getTranslation(getDefaultLocale());
        }
        return tr;
    }

    public Translation getDefaultTranslation() {
        return Translation.create(getDao().getDefaultTranslation());
    }

    public void setDefaultLocale(final Locale defaultLocale) {
        getDao().setDefaultLocale(defaultLocale);
    }

    public Locale getDefaultLocale() {
        return getDao().getDefaultLocale();
    }
}
