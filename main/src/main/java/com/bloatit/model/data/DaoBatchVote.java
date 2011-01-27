package com.bloatit.model.data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This class is for Hibernate only.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "actor_id", "batch_id" }) })
public final class DaoBatchVote extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoActor actor;

    @ManyToOne(optional = false)
    private DaoBatch batch;

    @Basic(optional = false)
    private boolean positif;

    protected DaoBatchVote() {
        super();
    }

    protected DaoBatchVote(DaoActor actor, DaoBatch batch, boolean positif) {
        super();
        this.actor = actor;
        this.batch = batch;
        this.positif = positif;
    }

    /**
     * @return the member
     */
    protected final DaoActor getActor() {
        return actor;
    }

    /**
     * @return the batch
     */
    protected final DaoBatch getBatch() {
        return batch;
    }

    /**
     * @return the positif
     */
    protected final boolean isPositif() {
        return positif;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batch == null) ? 0 : batch.hashCode());
        result = prime * result + ((actor == null) ? 0 : actor.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DaoBatchVote other = (DaoBatchVote) obj;
        if (batch == null) {
            if (other.batch != null) {
                return false;
            }
        } else if (!batch.equals(other.batch)) {
            return false;
        }
        if (actor == null) {
            if (other.actor != null) {
                return false;
            }
        } else if (!actor.equals(other.actor)) {
            return false;
        }
        return true;
    }

}
