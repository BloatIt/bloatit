package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.TranslationList;
import com.bloatit.model.data.DaoDescription;

public class Description extends Identifiable {

    private final DaoDescription dao;

    public Description(final Member member, final Locale locale, final String title, final String description) {
        super();
        this.dao = DaoDescription.createAndPersist(member.getDao(), locale, title, description);
    }

    public Description(final DaoDescription dao) {
        super();
        this.dao = dao;
    }

    public DaoDescription getDao() {
        return dao;
    }

    public PageIterable<Translation> getTranslations() {
        return new TranslationList(dao.getTranslationsFromQuery());
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
