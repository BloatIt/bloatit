package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    private final Set<DaoBatch> batches = new HashSet<DaoBatch>();

    /**
     * The expirationDate is calculated from the batches variables.
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date expirationDate;

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
     * @param expirationDate is the date when this offer should be finish. Must be non null,
     *        and in the future.
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws FatalErrorException if the amount is < 0 or if the Date is in the future.
     */
    public DaoOffer(final DaoMember member, final DaoDemand demand, final BigDecimal amount, final DaoDescription description, final Date dateExpire) {
        super(member);
        if (demand == null || description == null || dateExpire == null) {
            throw new NonOptionalParameterException();
        }
        this.demand = demand;
        this.amount = amount;
        this.expirationDate = dateExpire;
        addBatch(new DaoBatch(dateExpire, amount, description, this));
    }

    /**
     * @return All the batches for this offer. (Even the MasterBatch).
     */
    public PageIterable<DaoBatch> getBatches() {
        String query = "from DaoBatch where offer = :this order by expirationDate";
        String queryCount = "select count(*) from DaoBatch where offer = :this";
        return new QueryCollection<DaoBatch>( //
                SessionManager.createQuery(query).setEntity("this", this),//
                SessionManager.createQuery(queryCount).setEntity("this", this));//
    }

    public void addBatch(DaoBatch batch) {
        amount = batch.getAmount().add(amount);
        Date expiration = batch.getExpirationDate();
        if (expirationDate.before(expiration)){
            expirationDate = expiration;
        }
        batches.add(batch);
    }

    /**
     * @return a cloned version of the expirationDate attribute.
     */
    public Date getExpirationDate(){
        return (Date) expirationDate.clone();
    }

    public BigDecimal getAmount(){
        return amount;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoOffer() {
        super();
    }

    public DaoDemand getDemand(){
        return demand;
    }
}
