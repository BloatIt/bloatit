package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.TranslationList;
import com.bloatit.model.data.DaoDescription;

public class Description extends Identifiable {

    private final DaoDescription dao;

    public Description(DaoDescription dao) {
        super();
        this.dao = dao;
    }

    public DaoDescription getDao() {
        return dao;
    }

    public PageIterable<Translation> getTranslations() {
        return new TranslationList(dao.getTranslationsFromQuery());
    }

    public void addTranslation(Translation translation) {
        dao.addTranslation(translation.getDao());
    }

    public Translation getTranslation(Locale locale) {
        return Translation.create(dao.getTranslation(locale));
    }

    public Translation getDefaultTranslation() {
        return Translation.create(dao.getDefaultTranslation());
    }

    public void setDefaultLocale(Locale defaultLocale) {
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
