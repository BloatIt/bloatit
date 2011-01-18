package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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

    /**
     * Remember a description is a title with some content. (Translatable)
     */
    @OneToOne
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoDescription description;

    /**
     * Because the 'lot' is not already implemented I added a dateExpire field. It is
     * really not very useful...
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date dateExpire;

    /**
     * The amount represents the money the member want to have to make his offer. (this
     * will also probably will be modified by the 'lot' implementation)
     */
    @Basic(optional = false)
    private BigDecimal amount;

    /**
     * Create a DaoOffer.
     * 
     * @param member is the author of the offer. Must be non null.
     * @param demand is the demand on which this offer is made. Must be non null.
     * @param amount is the amount of the offer. Must be non null, and > 0.
     * @param text is the description of the demand. Must be non null.
     * @param dateExpire is the date when this offer should be finish. Must be non null,
     *        and in the future.
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws FatalErrorException if the amount is < 0 or if the Date is in the future.
     */
    public DaoOffer(final DaoMember member, final DaoDemand demand, final BigDecimal amount, final DaoDescription text, final Date dateExpire) {
        super(member);
        if (demand == null || text == null || dateExpire == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new FatalErrorException("Amount must be > 0");
        }
        if (dateExpire.before(new Date())) {
            throw new FatalErrorException("Make sure the date is in the future.");
        }
        this.demand = demand;
        this.description = text;
        this.dateExpire = (Date) dateExpire.clone();
        this.amount = amount;
    }

    public Date getDateExpire() {
        return (Date) dateExpire.clone();
    }

    public void setDateExpire(final Date dateExpire) {
        this.dateExpire = (Date) dateExpire.clone();
    }

    public DaoDemand getDemand() {
        return demand;
    }

    public DaoDescription getDescription() {
        return description;
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
}
