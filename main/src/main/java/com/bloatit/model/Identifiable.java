package com.bloatit.model;

import com.bloatit.data.IdentifiableInterface;
import com.bloatit.framework.exceptions.NonOptionalParameterException;

/**
 * An identifiable is the base class for each class that map a dao class.
 *
 * @author Thomas Guyard
 *
 * @param <T> is the dao being mapped.
 */
public abstract class Identifiable<T extends IdentifiableInterface> extends Unlockable implements IdentifiableInterface {

    private final T dao;

    protected Identifiable(final T dao) {
        if (dao == null) {
            throw new NonOptionalParameterException();
        }
        if (dao.getId() != null) {
            CacheManager.add(dao.getId(), this);
        }
        this.dao = dao;
    }

    /**
     * @return a unique identifier for this object.
     */
    @Override
    public final Integer getId() {
        return getDao().getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Identifiable<?> other = (Identifiable<?>) obj;
        if (getId() != other.getId()) {
            return false;
        }
        return true;
    }

    /**
     * @return the dao
     */
    public final T getDao() {
        return dao;
    }

}
