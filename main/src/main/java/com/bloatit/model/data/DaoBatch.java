package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.data.util.NonOptionalParameterException;

@Entity
public final class DaoBatch extends DaoIdentifiable {

    public enum State {
        PENDING, DEVELOPPING, DONE, CANCELED
    }

    /**
     * After this date, the Batch should be done.
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date expirationDate;

    /**
     * The amount represents the money the member want to have to make his offer.
     */
    @Basic(optional = false)
    private BigDecimal amount;

    @Basic(optional = false)
    @Enumerated
    private State state;

    @OneToMany(mappedBy = "batch")
    @Cascade(value = { CascadeType.ALL })
    private Set<DaoBatchVote> votes = new HashSet<DaoBatchVote>(0);

    /**
     * Remember a description is a title with some content. (Translatable)
     */
    @OneToOne
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoDescription description;

    @ManyToOne(optional = false)
    private DaoOffer offer;

    /**
     * Create a DaoOffer.
     *
     * @param amount is the amount of the offer. Must be non null, and > 0.
     * @param text is the description of the demand. Must be non null.
     * @param expirationDate is the date when this offer should be finish. Must be non
     *        null, and in the future.
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws FatalErrorException if the amount is < 0 or if the Date is in the future.
     */
    public DaoBatch(final Date dateExpire, final BigDecimal amount, final DaoDescription description, final DaoOffer offer) {
        super();
        if (dateExpire == null || amount == null || description == null || offer == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new FatalErrorException("Amount must be > 0");
        }
        if (dateExpire.before(new Date())) {
            throw new FatalErrorException("Make sure the date is in the future.");
        }
        this.expirationDate = (Date) dateExpire.clone();
        this.amount = amount;
        this.description = description;
        this.offer = offer;
        this.setState(State.PENDING);
    }

    public Date getExpirationDate() {
        return (Date) expirationDate.clone();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DaoDescription getDescription() {
        return description;
    }

    public DaoOffer getOffer() {
        return offer;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public boolean hasVoted(DaoActor actor) {
        // false is ignored in the equals and hashcode.
        return votes.contains(new DaoBatchVote(actor, this, false));
    }

    public void vote(DaoActor actor, boolean positif) {
        if (!hasVoted(actor)) {
            votes.add(new DaoBatchVote(actor, this, positif));
        } else {
            throw new FatalErrorException("Actor has already voted.");
        }
    }

    public int getVoteResult() {
        int result = 0;
        for (DaoBatchVote vote : votes) {
            if (vote.isPositif()) {
                result++;
            } else {
                result--;
            }
        }
        return result;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoBatch() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
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
        if (!(obj instanceof DaoBatch)) {
            return false;
        }
        DaoBatch other = (DaoBatch) obj;
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (expirationDate == null) {
            if (other.expirationDate != null) {
                return false;
            }
        } else if (!expirationDate.equals(other.expirationDate)) {
            return false;
        }
        return true;
    }
}
