package com.bloatit.framework;

public abstract class Identifiable extends Unlockable {

    /**
     * @return a unique identifier for this object.
     */
    public abstract int getId();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Identifiable other = (Identifiable) obj;
        if (getId() != other.getId())
            return false;
        return true;
    }

}
