package com.bloatit.model;

import com.bloatit.data.IdentifiableInterface;
import com.bloatit.framework.exceptions.NonOptionalParameterException;

public abstract class Identifiable<T extends IdentifiableInterface> extends Unlockable {

    protected T dao;

    protected Identifiable(final T id) {
        if (id == null) {
            throw new NonOptionalParameterException();
        }
        if (id.getId() != null) {
            CacheManager.add(id.getId(), this);
        }
        dao = id;
    }

    /**
     * @return a unique identifier for this object.
     */
    public final int getId(){
        return dao.getId();
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

}
