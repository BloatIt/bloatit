package com.bloatit.model;

import com.bloatit.data.DaoKudos;
import com.bloatit.data.DaoUserContent;

public final class Kudos extends UserContent<DaoKudos> {

    public static Kudos create(final DaoKudos dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoKudos> created = CacheManager.get(dao);
            if (created == null) {
                return new Kudos(dao);
            }
            return (Kudos) created;
        }
        return null;
    }

    private Kudos(final DaoKudos dao) {
        super(dao);
    }

    public int getValue() {
        return getDao().getValue();
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return getDao();
    }

}
