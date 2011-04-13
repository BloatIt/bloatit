package com.bloatit.model;

import com.bloatit.data.DaoIdentifiable;

/**
 * Helper class to easily create identifiable object from their Dao
 * representation.
 * 
 * @param <T> is the Type of object we want to create.
 * @param <DAO> is the Dao version of <code>T</code>.
 */
public abstract class Creator<DAO extends DaoIdentifiable, T extends Identifiable<DAO>> {

    public Creator() {
        // nothing to do.
    }

    /**
     * Create a Identifiable using its dao representation. If a correspondig
     * object is found in the cache, then there is no creation and the cached
     * object is returned.
     * 
     * @param dao is the dao version of the created object from the model layer
     * @return the created object or the object form the cache or null if not
     *         found.
     */
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

    /**
     * Abstract method to do the "new T(dao)", because generics are not
     * templates.
     * 
     * @param dao
     * @return the new T
     */
    public abstract T doCreate(DAO dao);
}
