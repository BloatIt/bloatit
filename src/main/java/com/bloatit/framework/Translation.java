package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoTranslation;

public class Translation extends Kudosable {

    private final DaoTranslation dao;

    public static Translation create(DaoTranslation dao) {
        if (dao == null) {
            return null;
        }
        return new Translation(dao);
    }

    private Translation(DaoTranslation dao) {
        super();
        this.dao = dao;
    }

    public DaoTranslation getDao() {
        return dao;
    }

    public String getTitle() {
        return dao.getTitle();
    }

    public Locale getLocale() {
        return dao.getLocale();
    }

    public String getText() {
        return dao.getText();
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
