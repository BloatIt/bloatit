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

@Entity
public class DaoOffer extends DaoKudosable {

    @ManyToOne(optional = false)
    private DaoDemand demand;

    @OneToOne
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    @IndexedEmbedded
    private DaoDescription description;

    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date dateExpire;

    @Basic(optional = false)
    private BigDecimal amount;

    protected DaoOffer() {
        super();
    }

    public DaoOffer(DaoMember member, DaoDemand Demand, BigDecimal amount, DaoDescription text, Date dateExpire) {
        super(member);
        demand = Demand;
        description = text;
        this.dateExpire = dateExpire;
        this.setAmount(amount);
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
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

    protected void setDemand(DaoDemand Demand) {
        demand = Demand;
    }

    protected void setDescription(DaoDescription text) {
        description = text;
    }

    protected void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
