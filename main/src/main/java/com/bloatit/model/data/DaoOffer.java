package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.NonOptionalParameterException;
import com.bloatit.model.data.util.SessionManager;

/**
 * An offer is a developer offer to a demand.
 */
@Entity
public final class DaoOffer extends DaoKudosable {

    /**
     * This is demand on which this offer is done.
     */
    @ManyToOne(optional = false)
    private DaoDemand demand;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    @OrderBy("expirationDate ASC")
    private final List<DaoBatch> batches = new ArrayList<DaoBatch>();

    /**
     * The expirationDate is calculated from the batches variables.
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date expirationDate;

    @Basic(optional = false)
    private int currentBatch;

    /**
     * The amount represents the money the member want to have to make his offer. This is
     * a calculated field used for performance speedup.
     * <code>(= foreach batches; amount += baches.getAmount)</code>
     */
    @Basic(optional = false)
    private BigDecimal amount;

    /**
     * Create a DaoOffer.
     *
     * @param member is the author of the offer. Must be non null.
     * @param demand is the demand on which this offer is made. Must be non null.
     * @param amount is the amount of the offer. Must be non null, and > 0.
     * @param description is the description of the demand. Must be non null.
     * @param expirationDate is the date when this offer should be finish. Must be non
     *        null, and in the future.
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws FatalErrorException if the amount is < 0 or if the Date is in the future.
     */
    public DaoOffer(final DaoMember member, final DaoDemand demand, final BigDecimal amount, final DaoDescription description, final Date dateExpire) {
        super(member);
        if (demand == null || description == null || dateExpire == null) {
            throw new NonOptionalParameterException();
        }
        this.demand = demand;
        this.amount = BigDecimal.ZERO; // Will be updated by addBatch
        this.expirationDate = dateExpire;
        addBatch(new DaoBatch(dateExpire, amount, description, this));
        this.currentBatch = 0;
    }

    /**
     * @return All the batches for this offer. (Even the MasterBatch).
     */
    public PageIterable<DaoBatch> getBatches() {
        final String query = "from DaoBatch where offer = :this order by expirationDate";
        final String queryCount = "select count(*) from DaoBatch where offer = :this";
        return new QueryCollection<DaoBatch>( //
                SessionManager.createQuery(query).setEntity("this", this),//
                SessionManager.createQuery(queryCount).setEntity("this", this));//
    }

    public void addBatch(final DaoBatch batch) {
        amount = batch.getAmount().add(amount);
        final Date expiration = batch.getExpirationDate();
        if (expirationDate.before(expiration)) {
            expirationDate = expiration;
        }
        batches.add(batch);
    }

    public DaoBatch getCurrentBatch(){
        return batches.get(currentBatch);
    }

    public boolean hasBatchesLeft(){
        return currentBatch < batches.size();
    }

    public void passToNextBatch(){
        currentBatch++;
    }

    public void cancelEverythingLeft(){
        currentBatch = batches.size();
    }

    /**
     * @return a cloned version of the expirationDate attribute.
     */
    public Date getExpirationDate() {
        return (Date) expirationDate.clone();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoOffer() {
        super();
    }

    public DaoDemand getDemand() {
        return demand;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batches == null) ? 0 : batches.hashCode());
        result = prime * result + ((demand == null) ? 0 : demand.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        if (!(obj instanceof DaoOffer)) {
            return false;
        }
        DaoOffer other = (DaoOffer) obj;
        if (batches == null) {
            if (other.batches != null) {
                return false;
            }
        } else if (!batches.equals(other.batches)) {
            return false;
        }
        if (demand == null) {
            if (other.demand != null) {
                return false;
            }
        } else if (!demand.equals(other.demand)) {
            return false;
        }
        return true;
    }
}
