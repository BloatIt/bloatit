package com.bloatit.model;

import com.bloatit.data.DaoIdentifiable;

public abstract class Creator<DAO extends DaoIdentifiable, T extends Identifiable<DAO>> {
    @SuppressWarnings("unchecked")
    public T create(final DAO dao) {
        if (dao != null) {
            final Identifiable<?> created = CacheManager.get(dao);
            if (created == null) {
                return doCreate(dao);
            }
            return (T) created;
        }
        return null;
    }

    public abstract T doCreate(DAO dao);
}
